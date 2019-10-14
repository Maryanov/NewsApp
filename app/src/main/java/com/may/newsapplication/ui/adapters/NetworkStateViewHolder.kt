package com.may.newsapplication.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.may.newsapplication.R
import kotlinx.android.synthetic.main.network_state_item.view.*

class NetworkStateViewHolder(view: View, private val retryCallback: () -> Unit
                                    ) : RecyclerView.ViewHolder(view) {

    init {
        itemView.retry_button.setOnClickListener { retryCallback() }
    }

    fun bindTo(networkState: NetworkState?) {
        //error message
        itemView.error_msg.visibility = if (networkState?.msg != null) View.VISIBLE else View.GONE
        if (networkState?.msg != null) {
            itemView.error_msg.text = networkState.msg
        }

        //loading and retry
        itemView.retry_button.visibility = if (networkState?.status == Status.FAILED) View.VISIBLE else View.GONE
        itemView.progress_bar.visibility = if (networkState?.status == Status.RUNNING) View.VISIBLE else View.GONE
    }

    companion object {
        fun create(parent: ViewGroup, retryCallback: () -> Unit): NetworkStateViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.network_state_item, parent, false)
            return NetworkStateViewHolder(view, retryCallback)
        }
    }

}