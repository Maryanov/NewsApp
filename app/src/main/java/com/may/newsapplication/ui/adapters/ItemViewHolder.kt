package com.may.newsapplication.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appnews.utils.GlideRequests
import com.may.newsapplication.extension.formatDateTime
import com.may.newsapplication.R
import com.may.newsapplication.model.NewsItem
import kotlinx.android.synthetic.main.item_list.view.*

class ItemViewHolder (view: View, private val glide: GlideRequests,
                      private val followTheLink: (item: NewsItem?) -> Unit
) : RecyclerView.ViewHolder(view) {

    var itemNews: NewsItem? = null

    init {
        itemView.setOnClickListener { followTheLink(itemNews) }
    }

    fun bindTo(item: NewsItem?) {

        itemNews = item

        itemView.title.text = item?.title ?: "заголовок"
        itemView.description.text = item?.description ?: "описание"
        itemView.date.text = item?.publishedAt?.formatDateTime() ?: "10.10.2014г."

        if (item?.urlToImage?.startsWith("http") == true) {
            itemView.thumbnail.visibility = View.VISIBLE
            glide.load(item.urlToImage)
                .centerCrop()
                .placeholder(R.drawable.ic_placeholder)
                .into(itemView.thumbnail)
        } else {
            itemView.thumbnail.visibility = View.GONE
            glide.clear(itemView.thumbnail)
        }
    }

    companion object {
        fun create(parent: ViewGroup, glide: GlideRequests, followTheLink: (item: NewsItem?) -> Unit
        ): ItemViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_list, parent, false)
            return ItemViewHolder(view, glide, followTheLink)
        }
    }

}