package com.may.newsapplication.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.may.newsapplication.R

class SplashActivity : AppCompatActivity() {

    private lateinit var statusStartActivityViewModel: SplashActivityViewModel

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()
        statusStartActivityViewModel = ViewModelProviders.of(this).get(SplashActivityViewModel::class.java)

        statusStartActivityViewModel.showNewsScreenLiveData.observe(this, Observer {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        })

        statusStartActivityViewModel.start()

        /*
        Observable.just(statusStartActivityViewModel.getData().value)
            .delay(1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .subscribe{
                when{
                    it!!.startsWith("PossibleToLaunch") ->{
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        */
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}

