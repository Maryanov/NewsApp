package com.may.newsapplication.paging

import androidx.paging.PageKeyedDataSource
import com.may.newsapplication.model.NewsItem
import com.may.newsapplication.services.INewsService
import androidx.lifecycle.MutableLiveData
import com.may.newsapplication.ui.adapters.NetworkState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class NewsDataSource(private val newsService: INewsService) : PageKeyedDataSource<Int, NewsItem>() {

    private val compositeDisposable = CompositeDisposable()

    val initialLoadingStateLiveData = MutableLiveData<NetworkState>()
    val rangeLoadingStateLiveData = MutableLiveData<NetworkState>()
    var cache: Map<Int, MutableList<NewsItem>> = linkedMapOf()
    var retry: (() -> Any)? = null

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, NewsItem>) {

        if (cache[1] != null) {
            callback.onResult(cache[1] as MutableList<NewsItem>, null, 2)
            return
        }

        val subscription = newsService.getNews(1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                initialLoadingStateLiveData.postValue(NetworkState.LOADING)
            }
            .doOnComplete {
                initialLoadingStateLiveData.postValue(NetworkState.LOADED)
            }
            .doOnError {
                initialLoadingStateLiveData.postValue(NetworkState.error(it.message))
            }
            .subscribe({result ->
                cache.plus(Pair(1,result))

                callback.onResult(result, null, 2)
            }, {
                retry = {
                    loadInitial(params, callback)
                }
            })

        compositeDisposable.add(subscription)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, NewsItem>) {

        if (cache[params.key] != null) {
            val nextKey = if (cache.size >= 5) null else cache.size + 1
            callback.onResult(cache[params.key] as MutableList<NewsItem>,  nextKey)
            return
        }


        val subscription = newsService.getNews(params.key)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { rangeLoadingStateLiveData.postValue(NetworkState.LOADING) }
            .doOnComplete { rangeLoadingStateLiveData.postValue(NetworkState.LOADED) }
            .doOnError { rangeLoadingStateLiveData.postValue(NetworkState.error(it.message)) }
            .subscribe({result ->
                val nextKey = if (params.key >= 5) null else params.key + 1

               cache.plus(Pair(params.key, result))

                callback.onResult(result, nextKey)
            }, {
                retry = {
                    loadAfter(params, callback)
                }
            })

        compositeDisposable.add(subscription)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, NewsItem>) {
    }

    fun getState(): MutableLiveData<NetworkState>{
        return initialLoadingStateLiveData
    }

    fun retryFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.invoke()
    }

    fun cleanup() {
        compositeDisposable.clear()
    }
}