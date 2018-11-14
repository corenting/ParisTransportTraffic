package fr.corenting.traficparis.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.corenting.traficparis.R
import kotlinx.android.synthetic.main.list_item.view.*
import fr.corenting.traficparis.models.ListItem
import fr.corenting.traficparis.utils.DrawableUtils


class MainAdapter(private val context: Context) :
    androidx.recyclerview.widget.RecyclerView.Adapter<MainAdapter.ResultViewHolder>() {

    private lateinit var recyclerView: RecyclerView
    private var dataSet = mutableListOf<ListItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return ResultViewHolder(v)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        val currentResult = dataSet[position]
        bindHolderContent(holder.itemView, currentResult)
    }

    private fun bindHolderContent(itemView: View, currentResult: ListItem) {
        itemView.titleTextView.text = context.getString(R.string.line_title, currentResult.lineName,
            currentResult.title)
        itemView.subtitleTextView.text = currentResult.stateDescription

        // Drawable
        val drawable = DrawableUtils.getDrawableForLine(
            context, currentResult.type,
            currentResult.lineName
        )
        if (drawable == null) {
            itemView.logoImageView.visibility = View.GONE
        } else {
            itemView.logoImageView.visibility = View.VISIBLE
            itemView.logoImageView.setImageDrawable(drawable)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    fun removeAllItems() {
        recyclerView.post {
            recyclerView.isEnabled = false
            val size = dataSet.size
            dataSet.clear()
            notifyItemRangeRemoved(0, size)
            recyclerView.isEnabled = true
        }
    }

    fun addItems(items: List<ListItem>) {
        recyclerView.post {
            recyclerView.isEnabled = false
            dataSet.addAll(items)
            notifyItemRangeInserted(0, items.size)
            recyclerView.isEnabled = true
        }
    }

    class ResultViewHolder(itemView: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView)
}