package com.may.newsapplication.api

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface IApiClient {

    @GET("everything?q=android&from=2019-04-00&sortBy=publi%20shedAt&apiKey=26eddb253e7840f988aec61f2ece2907")
    fun getNewsByPage(@Query("page") page: Int): Observable<NewsResponse>

}