package com.may.newsapplication.mappers

import com.may.newsapplication.api.NewsResponse
import com.may.newsapplication.model.NewsItem

interface INewsMapper {
    fun mapResponseToNews(source : NewsResponse) : List<NewsItem>
}