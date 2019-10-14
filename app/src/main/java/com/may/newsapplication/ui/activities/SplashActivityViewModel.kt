package com.may.newsapplication.ui.activities

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SplashActivityViewModel(application: Application) : AndroidViewModel(application) {

    private var data : MutableLiveData<String> = MutableLiveData()

    private var delaySubscription : Disposable? = null

    val showNewsScreenLiveData = MutableLiveData<Unit>()

    fun start() {
        delaySubscription = Observable.just(Unit)
            .delay(1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                showNewsScreenLiveData.value = Unit
            }
    }

    fun getData(): LiveData<String>{
        if (data.value==null) {
            data.value = "PossibleToLaunch"
        } else data.value = "AlreadyStarted"
        return data
    }

    override fun onCleared() {
        delaySubscription?.dispose()
        super.onCleared()
    }
}