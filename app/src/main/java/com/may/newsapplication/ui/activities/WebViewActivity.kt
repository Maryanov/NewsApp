package com.may.newsapplication.ui.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.may.newsapplication.R
import com.may.newsapplication.extension.adaptUrl
import kotlinx.android.synthetic.main.activity_show_news.*

class WebViewActivity : AppCompatActivity() {

    private var newsUrl : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.fadein,R.anim.fadeout) //плавный переход между Активити
        setTheme(R.style.AppTheme) // принудительно устанавливаем требуемый стиль
        setContentView(R.layout.activity_show_news)
        newsUrl = intent.getStringExtra("url") as String
        // ниже выполняется небольшая адаптация ссылки для того чтобы оставаться в приложении без перехода в браузер
        newsUrl = newsUrl?.adaptUrl()
        setupLayout()
    }

    private fun setupLayout() {

        webNews.visibility = View.VISIBLE
        webNews.settings.javaScriptEnabled = false
        webNews.settings.allowContentAccess=false
        webNews.settings.allowFileAccess = false
        webNews.settings.loadWithOverviewMode = false
        webNews.settings.useWideViewPort = false
        webNews.loadUrl(newsUrl)
    }
}
