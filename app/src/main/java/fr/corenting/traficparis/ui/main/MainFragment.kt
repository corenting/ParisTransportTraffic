package fr.corenting.traficparis.ui.main

import android.content.Context
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.mikepenz.itemanimators.SlideInOutLeftAnimator
import fr.corenting.traficparis.BuildConfig
import fr.corenting.traficparis.R
import fr.corenting.traficparis.databinding.MainFragmentBinding
import fr.corenting.traficparis.models.LineType
import fr.corenting.traficparis.models.RequestResult
import fr.corenting.traficparis.models.api.ApiResponse
import fr.corenting.traficparis.traffic.TrafficViewModel
import fr.corenting.traficparis.utils.MiscUtils
import fr.corenting.traficparis.utils.PersistenceUtils
import fr.corenting.traficparis.utils.ListUtils


class MainFragment : Fragment(R.layout.main_fragment), MenuProvider {

    private val viewModel: TrafficViewModel by activityViewModels()
    private lateinit var observer: Observer<RequestResult<ApiResponse>>

    private var displayRer = true
    private var displayMetro = true
    private var displayTramway = true
    private var displayTransilien = true

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Observable for refresh
        observer = Observer { result ->
            if (result == null) {
                displayErrorMessage()
            } else {
                // Show error message
                if (result.data == null || result.error != null) {
                    displayErrorMessage()
                } else {
                    endLoading(empty = false)

                    // Apply filter
                    setDisplayedContent(result.data)
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

        // Setup menu
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        return binding.root
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
            viewModel.getLatestTraffic()
        }
        binding.emptySwipeRefreshLayout.setOnRefreshListener(listener)
        binding.swipeRefreshLayout.setOnRefreshListener(listener)

        // Observe
        viewModel.getTraffic().observe(viewLifecycleOwner, observer)
    }


    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.fragment_menu, menu)

        // Update menu values for filters from shared prefs
        if (context != null) {
            displayRer = PersistenceUtils.getDisplayCategoryValue(requireContext(), LineType.RER)
            displayMetro =
                PersistenceUtils.getDisplayCategoryValue(requireContext(), LineType.METRO)
            displayTramway =
                PersistenceUtils.getDisplayCategoryValue(requireContext(), LineType.TRAMWAY)
            displayTransilien =
                PersistenceUtils.getDisplayCategoryValue(requireContext(), LineType.TRANSILIEN)

            val subMenu = menu.getItem(0)?.subMenu
            subMenu?.getItem(0)?.isChecked = displayRer
            subMenu?.getItem(1)?.isChecked = displayMetro
            subMenu?.getItem(2)?.isChecked = displayTramway
            subMenu?.getItem(3)?.isChecked = displayTransilien

        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            // About popup
            R.id.about_menu -> {
                showAboutPopup()
                return true
            }
            // Filter items
            R.id.filter_rer, R.id.filter_metro, R.id.filter_tramway, R.id.filter_transilien -> {
                menuItem.isChecked = !menuItem.isChecked
                LineType.values().find { menuItem.itemId == it.menuFilterId }?.let {
                    PersistenceUtils.setValue(
                        activity as Context,
                        it,
                        menuItem.isChecked
                    )
                }
                changeDisplayedCategories(menuItem.itemId, menuItem.isChecked)
                return true
            }
            else -> {
                return false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun displayErrorMessage() {
        endLoading(empty = true)
        Snackbar.make(
            binding.container, getString(R.string.download_error),
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun setDisplayedContent(results: ApiResponse) {
        val filteredResults = ListUtils.mapAndFilterResults(
            results, displayRer, displayMetro, displayTramway, displayTransilien
        )

        try {
            (binding.recyclerView.adapter as MainAdapter)
                .submitList(filteredResults)
        } catch (pass: IllegalArgumentException) {
            displayErrorMessage()
        }
    }

    private fun changeDisplayedCategories(filterId: Int, newValue: Boolean) {
        when (filterId) {
            R.id.filter_rer -> displayRer = newValue
            R.id.filter_metro -> displayMetro = newValue
            R.id.filter_tramway -> displayTramway = newValue
            R.id.filter_transilien -> displayTransilien = newValue
        }

        viewModel.getTraffic().value?.data?.let { setDisplayedContent(it) }
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
        binding.emptySwipeRefreshLayout.post {
            binding.emptySwipeRefreshLayout.visibility = View.GONE
        }
        binding.swipeRefreshLayout.post {
            binding.swipeRefreshLayout.visibility = View.VISIBLE
            binding.swipeRefreshLayout.isRefreshing = true
        }
    }

}
