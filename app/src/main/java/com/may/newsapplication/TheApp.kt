package com.may.newsapplication

import android.app.Application
import com.may.newsapplication.di.Container

class TheApp : Application() {

    lateinit var container : Container

    override fun onCreate() {
        super.onCreate()
        container = Container()
    }

}