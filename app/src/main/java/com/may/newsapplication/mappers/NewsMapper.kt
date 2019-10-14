package com.may.newsapplication.mappers

import com.may.newsapplication.api.NewsResponse
import com.may.newsapplication.model.NewsItem

class NewsMapper : INewsMapper {

    override fun mapResponseToNews(source : NewsResponse) : List<NewsItem> {
        return source.articles?.map {
            NewsItem(it.urlToImage ?: "",
                    it.title ?: "",
                     it.url ?: "",
                it.description ?: "",
                it.publishedAt ?: "")
        } ?: emptyList()
    }

}