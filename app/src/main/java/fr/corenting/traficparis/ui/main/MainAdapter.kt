package fr.corenting.traficparis.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.corenting.traficparis.R
import kotlinx.android.synthetic.main.list_item.view.*
import fr.corenting.traficparis.models.ListItem
import fr.corenting.traficparis.models.ListTitle
import fr.corenting.traficparis.models.TitleType
import fr.corenting.traficparis.utils.DrawableUtils
import kotlinx.android.synthetic.main.list_title.view.*


class MainAdapter(private val context: Context) :
    androidx.recyclerview.widget.RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_TITLE = 0
        const val TYPE_ITEM = 1
    }

    private lateinit var recyclerView: RecyclerView
    private var dataSet = mutableListOf<Any>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_TITLE) {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_title, parent, false)
            HeaderViewHolder(v)
        } else {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item, parent, false)
            ItemViewHolder(v)
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun getItemViewType(position: Int): Int {
        val currentResult = dataSet[position]
        if (currentResult is ListTitle) {
            return TYPE_TITLE
        }
        return TYPE_ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentResult = dataSet[position]
        if (getItemViewType(position) == TYPE_ITEM) {
            bindItemHolder(holder.itemView, currentResult as ListItem)
        } else {
            bindTitleHolder(holder.itemView, currentResult as ListTitle)
        }
    }

    private fun bindItemHolder(itemView: View, currentResult: ListItem) {
        itemView.titleTextView.text = context.getString(
            R.string.line_title, currentResult.lineName,
            currentResult.title
        )
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

    private fun bindTitleHolder(itemView: View, currentResult: ListTitle) {
        val title: String = when {
            currentResult.title == TitleType.OK -> context.getString(R.string.normal_traffic)
            currentResult.title == TitleType.WORK -> context.getString(R.string.work)
            else -> context.getString(R.string.issues)
        }
        itemView.headerTitleTextView.text = title
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

    fun addItems(items: List<Any>) {
        recyclerView.post {
            recyclerView.isEnabled = false
            dataSet.addAll(items)
            notifyItemRangeInserted(0, items.size)
            recyclerView.isEnabled = true
        }
    }

    class ItemViewHolder(itemView: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView)

    class HeaderViewHolder(itemView: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView)
}