package com.may.newsapplication.di

import com.may.newsapplication.api.ApiFactory
import com.may.newsapplication.api.IApiClient
import com.may.newsapplication.mappers.INewsMapper
import com.may.newsapplication.mappers.NewsMapper
import com.may.newsapplication.mvp.presenters.NewsPresenter
import com.may.newsapplication.paging.INewsDataFactory
import com.may.newsapplication.paging.NewsDataFactory
import com.may.newsapplication.services.INewsService
import com.may.newsapplication.services.NewsService
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import org.kodein.di.newInstance

class Container {

    private val kodein = Kodein {
        bind<IApiClient>() with singleton { ApiFactory().createApiClient() }
        bind<INewsMapper>() with singleton { NewsMapper() }
        bind<INewsService>() with singleton { NewsService(instance(), instance()) }
        bind<INewsDataFactory>() with singleton { NewsDataFactory(instance()) }
    }

    val newsPresenter : NewsPresenter by kodein.newInstance { NewsPresenter(instance()) }
}