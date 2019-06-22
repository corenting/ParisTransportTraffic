package fr.corenting.traficparis.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import fr.corenting.traficparis.R
import kotlinx.android.synthetic.main.list_item.view.*
import fr.corenting.traficparis.models.ListItem
import fr.corenting.traficparis.models.ListTitle
import fr.corenting.traficparis.models.TitleType
import fr.corenting.traficparis.utils.DrawableUtils
import kotlinx.android.synthetic.main.list_title.view.*


class MainAdapter(private val context: Context) :
    ListAdapter<Any, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<Any>() {
        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            if (oldItem is ListItem && newItem is ListItem) {
                return oldItem == newItem
            }

            if (oldItem is ListTitle && newItem is ListTitle) {
                return oldItem == newItem
            }

            return false
        }

        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            if (oldItem is ListItem && newItem is ListItem) {
                return oldItem.type == newItem.type && oldItem.lineName == newItem.lineName
            }

            if (oldItem is ListTitle && newItem is ListTitle) {
                return oldItem.title == newItem.title
            }

            return false
        }
    }) {

    companion object {
        const val TYPE_TITLE = 0
        const val TYPE_ITEM = 1
    }

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

    override fun getItemViewType(position: Int): Int {
        val currentResult = getItem(position)
        if (currentResult is ListTitle) {
            return TYPE_TITLE
        }
        return TYPE_ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentResult = getItem(position)
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

    class ItemViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView)

    class HeaderViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView)
}