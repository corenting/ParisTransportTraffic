package fr.corenting.traficparis.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.mikepenz.itemanimators.*
import fr.corenting.traficparis.R
import kotlinx.android.synthetic.main.main_fragment.*
import fr.corenting.traficparis.models.ApiResponseResults
import fr.corenting.traficparis.traffic.TrafficViewModel
import fr.corenting.traficparis.utils.ResultsSorting


class MainFragment : androidx.fragment.app.Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: TrafficViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Init recyclerview
        recyclerView.itemAnimator = SlideInOutLeftAnimator(recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = MainAdapter(context!!)

        startLoading()

        // Refresh observable
        val observer = Observer<ApiResponseResults> {
            if (it == null) {
                endLoading(empty = true)
                Snackbar.make(container, getString(R.string.download_error), Snackbar.LENGTH_SHORT).show()
            } else {
                endLoading(empty = false)
                recyclerView.layoutManager = LinearLayoutManager(context)
                (recyclerView.adapter as MainAdapter).addItems(ResultsSorting.convertApiResultsToListItems(it))
            }
        }

        // View model
        viewModel = ViewModelProviders.of(this).get(TrafficViewModel::class.java)

        // Add listeners
        val listener = {
            startLoading()
            viewModel.getTraffic().observe(this, observer)
        }
        emptySwipeRefreshLayout.setOnRefreshListener(listener)
        swipeRefreshLayout.setOnRefreshListener(listener)

        // Load data
        viewModel.getTraffic().observe(this, observer)
    }

    private fun endLoading(empty: Boolean) {
        emptySwipeRefreshLayout.post {
            emptySwipeRefreshLayout.visibility = if (empty) View.VISIBLE else View.GONE
            emptySwipeRefreshLayout.isRefreshing = false
        }
        swipeRefreshLayout.post {
            swipeRefreshLayout.visibility = if (empty) View.GONE else View.VISIBLE
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun startLoading() {
        (recyclerView.adapter as MainAdapter).removeAllItems()
        emptySwipeRefreshLayout.post { emptySwipeRefreshLayout.visibility = View.GONE }
        swipeRefreshLayout.post {
            swipeRefreshLayout.visibility = View.VISIBLE
            swipeRefreshLayout.isRefreshing = true
        }
    }
}
