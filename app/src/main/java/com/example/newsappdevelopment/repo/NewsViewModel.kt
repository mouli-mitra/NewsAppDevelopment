package com.example.newsappdevelopment.repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsappdevelopment.datamodel.Article

class NewsViewModel : ViewModel() {

    private var newsLiveData: MutableLiveData<List<Article>>? = null

    fun getNews(category: String?, ctx: Context): MutableLiveData<List<Article>>? {


        newsLiveData = category.let {
            NewsRepository().getNewsApiCall(it, ctx)
        }

        return newsLiveData
    }


}