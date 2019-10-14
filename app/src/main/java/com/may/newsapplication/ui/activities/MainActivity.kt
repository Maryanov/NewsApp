package com.may.newsapplication.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.appnews.utils.GlideApp
import com.may.newsapplication.R
import com.may.newsapplication.TheApp
import com.may.newsapplication.mvp.presenters.NewsPresenter
import com.may.newsapplication.mvp.views.INewsView
import com.may.newsapplication.ui.adapters.NetworkState
import com.may.newsapplication.ui.adapters.NewsRecyclerViewAdapter
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), INewsView {

    private lateinit var newsListViewModel: NewsListViewModel
    private lateinit var newsPresenter : NewsPresenter
    private val glideRequests by lazy { GlideApp.with(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.fadein,R.anim.fadeout)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)

        newsSwipeRefreshLayout.setOnRefreshListener {
            Observable.just("")
                .delay(2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .subscribe{
                    newsSwipeRefreshLayout.isRefreshing = false 
                }
            newsPresenter.start() }

        newsListViewModel = ViewModelProviders.of(this).get(NewsListViewModel::class.java)

        newsRecyclerView.addItemDecoration(SimpleDividerItemDecoration(this))

        newsPresenter = (application as TheApp).container.newsPresenter
        newsPresenter.attachView(this)

        newsPresenter.newDataProviderLiveData.observe(this, Observer { dataProvider ->
            val newsAdapter = NewsRecyclerViewAdapter(glideRequests, newsPresenter::onItemSelected){
                dataProvider.retry()
            }

            dataProvider.initialNetworkState.observe(this, Observer {initialState ->
                if (initialState== NetworkState.LOADED) {
                    newsSwipeRefreshLayout.isRefreshing = false
                    progress_bar_init.visibility = View.GONE
                }
            })

            dataProvider.rangeNetworkState.observe(this, Observer {state ->
                newsAdapter.setNetworkState(state)
            })

            dataProvider.pagedList.observe(this, Observer { pagedList ->
                newsAdapter.submitList(pagedList)
            })
            newsRecyclerView.adapter = newsAdapter
        })

        newsPresenter.start()
         
    }

    override fun showDetails(itemUrl : String) {
        val intent = Intent(this, WebViewActivity::class.java)
        intent.putExtra("url", itemUrl)
        startActivity(intent)
    }

    override fun onDestroy() {
        newsPresenter.detachView()
        super.onDestroy()
    }

}
