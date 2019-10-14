package com.may.newsapplication.services

import com.may.newsapplication.api.IApiClient
import com.may.newsapplication.mappers.INewsMapper
import com.may.newsapplication.model.NewsItem
import io.reactivex.Observable

class NewsService(private val apiClient: IApiClient, private val newsMapper: INewsMapper) : INewsService {

    override fun getNews(pageNumber : Int) : Observable<List<NewsItem>> {
        return apiClient.getNewsByPage(pageNumber)
            .map { response ->
                newsMapper.mapResponseToNews(response)
            }

    }

}