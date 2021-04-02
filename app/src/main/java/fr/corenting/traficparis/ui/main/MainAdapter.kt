package fr.corenting.traficparis.ui.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import fr.corenting.traficparis.R
import fr.corenting.traficparis.databinding.ListItemBinding
import fr.corenting.traficparis.databinding.ListTitleBinding
import fr.corenting.traficparis.models.list.ListItem
import fr.corenting.traficparis.models.list.ListTitle
import fr.corenting.traficparis.models.TitleType
import fr.corenting.traficparis.utils.DrawableUtils


class MainAdapter :
    ListAdapter<Any, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<Any>() {

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            if (oldItem is ListItem && newItem is ListItem) {
                return oldItem == newItem
            }

            if (oldItem is ListTitle && newItem is ListTitle) {
                return false
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
            val itemViewBinding = ListItemBinding.bind(holder.itemView)
            bindItemHolder(itemViewBinding, currentResult as ListItem)
        } else {
            val itemViewBinding = ListTitleBinding.bind(holder.itemView)
            bindTitleHolder(itemViewBinding, currentResult as ListTitle)
        }
    }

    private fun bindItemHolder(itemViewBinding: ListItemBinding, currentResult: ListItem) {
        val context = itemViewBinding.root.context

        itemViewBinding.titleTextView.text = context.getString(
            R.string.line_title, currentResult.lineName,
            currentResult.title
        )
        itemViewBinding.subtitleTextView.text = currentResult.stateDescription

        // Drawable
        val drawable = DrawableUtils.getDrawableForLine(
            context, currentResult.type,
            currentResult.lineName
        )
        if (drawable == null) {
            itemViewBinding.logoImageView.visibility = View.GONE
        } else {
            itemViewBinding.logoImageView.visibility = View.VISIBLE
            itemViewBinding.logoImageView.setImageDrawable(drawable)
        }
    }

    private fun bindTitleHolder(itemViewBinding: ListTitleBinding, currentResult: ListTitle) {
        val context = itemViewBinding.root.context
        val title: String = when (currentResult.title) {
            TitleType.OK -> context.getString(R.string.normal_traffic)
            TitleType.WORK -> context.getString(R.string.work)
            else -> context.getString(R.string.issues)
        }
        itemViewBinding.headerTitleTextView.text = title
    }

    class ItemViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView)

    class HeaderViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView)
}