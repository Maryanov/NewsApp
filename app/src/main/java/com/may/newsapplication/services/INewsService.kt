package com.may.newsapplication.services

import com.may.newsapplication.model.NewsItem
import io.reactivex.Observable

interface INewsService {
    fun getNews(pageNumber : Int) : Observable<List<NewsItem>>
}