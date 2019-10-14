package com.may.newsapplication.paging

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.may.newsapplication.model.NewsItem
import com.may.newsapplication.services.INewsService
import com.may.newsapplication.ui.adapters.NetworkState

class NewsDataFactory(private val newsService: INewsService) : DataSource.Factory<Int, NewsItem>(), INewsDataFactory{

    private val newsDataSourceLiveData = MutableLiveData<NewsDataSource>()
    private var newsDataSource : NewsDataSource? = null

    override fun create(): DataSource<Int, NewsItem> {
        newsDataSource = NewsDataSource(newsService)
        newsDataSourceLiveData.postValue(newsDataSource)
        return newsDataSource!!
    }

    private fun getPagedListLiveData() : LiveData<PagedList<NewsItem>> {
        val pagedListConfig = PagedList.Config.Builder()
            .setPageSize(20)
            .setEnablePlaceholders(true)
            .build()

        return LivePagedListBuilder(this, pagedListConfig)
            .build()
    }

    override fun getDataProvider() : PagingDataProvider<NewsItem> {
        val pagedList = getPagedListLiveData()
        return PagingDataProvider(pagedList,
            Transformations.switchMap(newsDataSourceLiveData) { it.initialLoadingStateLiveData },
            Transformations.switchMap(newsDataSourceLiveData) { it.rangeLoadingStateLiveData },
            { newsDataSourceLiveData.value?.retryFailed() },
            { newsDataSourceLiveData.value?.cleanup()})
    }

}

class PagingDataProvider<T : Any>(
    val pagedList: LiveData<PagedList<T>>,
    val initialNetworkState: LiveData<NetworkState>,
    val rangeNetworkState: LiveData<NetworkState>,
    val retry: () -> Unit,
    val cleanup : () -> Unit)