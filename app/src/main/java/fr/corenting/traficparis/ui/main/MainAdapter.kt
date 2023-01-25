package fr.corenting.traficparis.ui.main

import android.annotation.SuppressLint
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import fr.corenting.traficparis.R
import fr.corenting.traficparis.databinding.ListItemBinding
import fr.corenting.traficparis.databinding.ListNoDataBinding
import fr.corenting.traficparis.databinding.ListTitleBinding
import fr.corenting.traficparis.models.LineState
import fr.corenting.traficparis.models.list.ListLineItem
import fr.corenting.traficparis.models.list.ListTitleItem
import fr.corenting.traficparis.models.list.ListItemInterface
import fr.corenting.traficparis.utils.DrawableUtils
import fr.corenting.traficparis.utils.MiscUtils.htmlToSpanned


class MainAdapter :
    ListAdapter<ListItemInterface, RecyclerView.ViewHolder>(object :
        DiffUtil.ItemCallback<ListItemInterface>() {

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: ListItemInterface,
            newItem: ListItemInterface
        ): Boolean {
            if (oldItem is ListLineItem && newItem is ListLineItem) {
                return oldItem == newItem
            }

            if (oldItem is ListTitleItem && newItem is ListTitleItem) {
                return false
            }

            return false
        }

        override fun areItemsTheSame(
            oldItem: ListItemInterface,
            newItem: ListItemInterface
        ): Boolean {
            if (oldItem is ListLineItem && newItem is ListLineItem) {
                return oldItem.type == newItem.type && oldItem.name == newItem.name
            }

            if (oldItem is ListTitleItem && newItem is ListTitleItem) {
                return oldItem.title == newItem.title
            }

            return false
        }
    }) {

    companion object {
        const val TYPE_TITLE = 0
        const val TYPE_ITEM = 1
        const val TYPE_NO_DATA = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_TITLE -> {
                val v = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_title, parent, false)
                HeaderViewHolder(v)
            }
            TYPE_ITEM -> {
                val v = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item, parent, false)
                ItemViewHolder(v)
            }
            else -> {
                val v = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_no_data, parent, false)
                ItemViewHolder(v)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ListTitleItem -> {
                TYPE_TITLE
            }
            is ListLineItem -> {
                TYPE_ITEM
            }
            else -> TYPE_NO_DATA
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentResult = getItem(position)

        when {
            getItemViewType(position) == TYPE_ITEM -> {
                val itemViewBinding = ListItemBinding.bind(holder.itemView)
                bindItemHolder(itemViewBinding, currentResult as ListLineItem)
            }
            getItemViewType(position) == TYPE_TITLE -> {
                val itemViewBinding = ListTitleBinding.bind(holder.itemView)
                bindTitleHolder(itemViewBinding, currentResult as ListTitleItem)
            }
            getItemViewType(position) == TYPE_NO_DATA  -> {
                val itemViewBinding = ListNoDataBinding.bind(holder.itemView)
                bindNoDataHolder(itemViewBinding)
            }
        }
    }

    private fun bindItemHolder(itemViewBinding: ListItemBinding, currentResult: ListLineItem) {
        val context = itemViewBinding.root.context

        itemViewBinding.titleTextView.text = context.getString(
            R.string.line_title, currentResult.name,
            currentResult.title
        )

        itemViewBinding.messageTextView.text = htmlToSpanned(currentResult.message)
        itemViewBinding.messageTextView.movementMethod = LinkMovementMethod.getInstance();

        // Drawable
        val drawable = DrawableUtils.getDrawableForLine(
            context, currentResult.type,
            currentResult.name
        )
        if (drawable == null) {
            itemViewBinding.logoImageView.visibility = View.INVISIBLE
        } else {
            itemViewBinding.logoImageView.visibility = View.VISIBLE
            itemViewBinding.logoImageView.setImageDrawable(drawable)
        }
    }

    private fun bindTitleHolder(itemViewBinding: ListTitleBinding, currentResult: ListTitleItem) {
        val context = itemViewBinding.root.context
        val title: String = when (currentResult.title) {
            LineState.WORK -> context.getString(R.string.work)
            else -> context.getString(R.string.issues)
        }
        itemViewBinding.headerTitleTextView.text = title
    }

    private fun bindNoDataHolder(itemViewBinding: ListNoDataBinding) {
        val context = itemViewBinding.root.context
        itemViewBinding.textTextView.text = context.getString(R.string.no_data_issues)
    }

    class ItemViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView)

    class HeaderViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView)
}