package com.may.newsapplication.paging

import com.may.newsapplication.model.NewsItem

interface INewsDataFactory {
    fun getDataProvider() : PagingDataProvider<NewsItem>
}