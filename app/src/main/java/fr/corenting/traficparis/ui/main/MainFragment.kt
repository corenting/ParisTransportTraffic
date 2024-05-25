package fr.corenting.traficparis.ui.main

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
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
import fr.corenting.traficparis.utils.ListUtils
import fr.corenting.traficparis.utils.MiscUtils


class MainFragment : Fragment(R.layout.main_fragment), MenuProvider {

    private val viewModel: TrafficViewModel by activityViewModels()
    private lateinit var apiDataObserver: Observer<RequestResult<ApiResponse>>

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Observable for refresh
        apiDataObserver = Observer { result ->
            if (result.data == null || result.error != null) {
                displayErrorMessage()
            } else {
                endLoading(empty = false)
                setDisplayedContent(result.data)
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
            viewModel.refreshTrafficData()
        }
        binding.emptySwipeRefreshLayout.setOnRefreshListener(listener)
        binding.swipeRefreshLayout.setOnRefreshListener(listener)

        // Observe
        viewModel.getTrafficData().observe(viewLifecycleOwner, apiDataObserver)
    }


    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.fragment_menu, menu)

        val subMenu = menu.getItem(0)?.subMenu
        val displayFilters = viewModel.getDisplayFilters()

        for (lineType in LineType.entries) {
            subMenu?.findItem(lineType.menuFilterId)?.isChecked = displayFilters[lineType] ?: true
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
                val newValue = !menuItem.isChecked
                menuItem.isChecked = newValue
                LineType.entries.find { menuItem.itemId == it.menuFilterId }?.let {
                    viewModel.updateDisplayFilterValue(it, newValue)
                    viewModel.getTrafficData().value?.data?.let { data -> setDisplayedContent(data) }
                }
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

    private fun setDisplayedContent(
        results: ApiResponse
    ) {
        val filteredResults = ListUtils.mapAndFilterResults(
            results, viewModel.getDisplayFilters()
        )

        try {
            (binding.recyclerView.adapter as MainAdapter)
                .submitList(filteredResults)
        } catch (pass: IllegalArgumentException) {
            displayErrorMessage()
        }
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
