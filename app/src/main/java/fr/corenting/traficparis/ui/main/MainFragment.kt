package fr.corenting.traficparis.ui.main

import android.content.Context
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.mikepenz.itemanimators.SlideInOutLeftAnimator
import fr.corenting.traficparis.BuildConfig
import fr.corenting.traficparis.R
import fr.corenting.traficparis.databinding.MainFragmentBinding
import fr.corenting.traficparis.models.ApiResponse
import fr.corenting.traficparis.models.RequestResult
import fr.corenting.traficparis.traffic.TrafficViewModel
import fr.corenting.traficparis.utils.MiscUtils
import fr.corenting.traficparis.utils.PersistenceUtils
import fr.corenting.traficparis.utils.ResultsUtils


class MainFragment : androidx.fragment.app.Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: TrafficViewModel by activityViewModels()
    private lateinit var observer: Observer<MutableLiveData<RequestResult<ApiResponse>>>

    private var displayRer = true
    private var displayMetro = true
    private var displayTram = true

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        // Observable for refresh
        observer = Observer {
            val result = it.value
            if (result == null) {
                endLoading(empty = true)
                displayErrorMessage()
            } else {
                // Show error message
                if (result.data == null || result.error != null || result.data.result.message == "Something went wrong") {
                    displayErrorMessage()
                } else {
                    endLoading(empty = false)

                    // Apply filter
                    val filteredResults = ResultsUtils.filterResults(
                        result.data.result, displayRer, displayMetro, displayTram
                    )

                    try {
                        (binding.recyclerView.adapter as MainAdapter)
                            .submitList(ResultsUtils.convertApiResultsToListItems(filteredResults))
                    } catch (pass: IllegalArgumentException) {
                        displayErrorMessage()
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

        // Update menu values for filters from shared prefs
        if (context != null) {
            displayRer = PersistenceUtils.getDisplayRerValue(activity as Context)
            displayMetro = PersistenceUtils.getDisplayMetroValue(activity as Context)
            displayTram = PersistenceUtils.getDisplayTramValue(activity as Context)

            val subMenu = menu.getItem(0)?.subMenu
            subMenu?.getItem(0)?.isChecked = displayRer
            subMenu?.getItem(1)?.isChecked = displayMetro
            subMenu?.getItem(2)?.isChecked = displayTram

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.about_menu -> showAboutPopup()
            R.id.filter_rer, R.id.filter_metro, R.id.filter_tram -> {
                item.isChecked = !item.isChecked
                PersistenceUtils.setValue(activity as Context, item.itemId, item.isChecked)
                changeDisplayedCategories(item.itemId, item.isChecked)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Init recyclerview
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = MainAdapter()
        binding.recyclerView.itemAnimator = SlideInOutLeftAnimator(binding.recyclerView)

        startLoading()

        // Add listeners
        val listener = {
            startLoading()
            viewModel.getUpdatedTraffic().observe(viewLifecycleOwner, observer)
        }
        binding.emptySwipeRefreshLayout.setOnRefreshListener(listener)
        binding.swipeRefreshLayout.setOnRefreshListener(listener)

        // Load data
        when (savedInstanceState) {
            null -> {
                viewModel.getUpdatedTraffic().observe(viewLifecycleOwner, observer)
            }
            else -> {
                viewModel.getTraffic().observe(viewLifecycleOwner, observer)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun displayErrorMessage() {
        Snackbar.make(
            binding.container, getString(R.string.download_error),
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun changeDisplayedCategories(filterId: Int, newValue: Boolean) {
        when (filterId) {
            R.id.filter_rer -> displayRer = newValue
            R.id.filter_metro -> displayMetro = newValue
            R.id.filter_tram -> displayTram = newValue
        }

        // Refresh the display
        startLoading()
        viewModel.getTraffic().observe(this, observer)
    }

    private fun showAboutPopup() {
        val currentContext = context
        if (currentContext != null) {
            val dialog = AlertDialog.Builder(currentContext)
                .setTitle(R.string.app_name)
                .setIcon(R.mipmap.ic_launcher)
                .setMessage(
                    MiscUtils.htmlToSpanned(
                        getString(R.string.about_text) +
                                BuildConfig.VERSION_NAME
                    )
                )
                .setNegativeButton("OK") { dialog, _ -> dialog.dismiss() }
                .create()

            dialog.show()
            (dialog.findViewById<TextView>(android.R.id.message))?.movementMethod =
                LinkMovementMethod.getInstance()
        }
    }

    private fun endLoading(empty: Boolean) {
        if (empty) {
            (binding.recyclerView.adapter as MainAdapter).submitList(emptyList())
        }

        binding.emptySwipeRefreshLayout.post {
            binding.emptySwipeRefreshLayout.visibility = if (empty) View.VISIBLE else View.GONE
            binding.emptySwipeRefreshLayout.isRefreshing = false
        }
        binding.swipeRefreshLayout.post {
            binding.swipeRefreshLayout.visibility = if (empty) View.GONE else View.VISIBLE
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun startLoading() {
        binding.emptySwipeRefreshLayout.post { binding.emptySwipeRefreshLayout.visibility = View.GONE }
        binding.swipeRefreshLayout.post {
            binding.swipeRefreshLayout.visibility = View.VISIBLE
            binding.swipeRefreshLayout.isRefreshing = true
        }
    }
}
