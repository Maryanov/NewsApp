package com.may.newsapplication.mvp.presenters

import androidx.lifecycle.MutableLiveData
import com.may.newsapplication.model.NewsItem
import com.may.newsapplication.mvp.views.INewsView
import com.may.newsapplication.paging.INewsDataFactory
import com.may.newsapplication.paging.PagingDataProvider


class NewsPresenter(private val newsDataFactory: INewsDataFactory) {

    private var view : INewsView? = null

    val newDataProviderLiveData = MutableLiveData<PagingDataProvider<NewsItem>>()

    fun start() {
        newDataProviderLiveData.value = newsDataFactory.getDataProvider()
    }

    fun onItemSelected(newsItem: NewsItem?) {
        if (newsItem != null) {
           view?.showDetails(newsItem.url)
        }
    }

    fun attachView(view : INewsView) {
        this.view = view
    }

    fun detachView() {
        view = null
        newDataProviderLiveData.value?.cleanup?.invoke()
    }

}