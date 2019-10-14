package com.may.newsapplication.ui.adapters

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.appnews.utils.GlideRequests
import com.may.newsapplication.R
import com.may.newsapplication.model.NewsItem

class NewsRecyclerViewAdapter(private val glideRequests: GlideRequests,
                              private val followTheLink: (NewsItem?) -> Unit,
                              private val retryCallback: () -> Unit) :
    PagedListAdapter<NewsItem, RecyclerView.ViewHolder>( object : DiffUtil.ItemCallback<NewsItem?>() {
    override fun areItemsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
        return oldItem.url == newItem.url
    }

    override fun areContentsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
        return oldItem.url == newItem.url
                && oldItem.title == newItem.title
    }
}) {
    //private var networkState: NetworkState = NetworkState.LOADING

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_list -> ItemViewHolder.create(parent, glideRequests, followTheLink)
            R.layout.network_state_item -> NetworkStateViewHolder.create(parent, retryCallback)
            else -> throw IllegalArgumentException("unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_list -> (holder as? ItemViewHolder)?.bindTo(getItem(position))
            R.layout.network_state_item -> (holder as? NetworkStateViewHolder)?.bindTo(networkState)
        }
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.network_state_item
        } else {
            R.layout.item_list
        }
    }

    fun setNetworkState(newNetworkState: NetworkState?) {
        this.networkState
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

}