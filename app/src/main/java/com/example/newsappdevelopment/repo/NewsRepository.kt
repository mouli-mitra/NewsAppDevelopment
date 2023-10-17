package com.example.newsappdevelopment.repo

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.newsappdevelopment.MainActivity
import com.example.newsappdevelopment.R
import com.example.newsappdevelopment.datamodel.Article
import com.example.newsappdevelopment.datamodel.NewsData
import com.example.newsappdevelopment.datamodel.Source
import com.example.newsappdevelopment.retrofit.NewsApi
import com.example.newsappdevelopment.retrofit.RetrofitHelper
import com.squareup.picasso.BuildConfig
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsRepository {

    fun getNewsApiCall(category: String?, context: Context): MutableLiveData<List<Article>> {
        val newsList = MutableLiveData<List<Article>>()

        val call = RetrofitHelper.getInstance().create(NewsApi::class.java)
            .getNews("in", category, context.getString(R.string.Key)) //put your api key here

        call.enqueue(object : Callback<NewsData> {
            override fun onResponse(
                call: Call<NewsData>,
                response: Response<NewsData>
            ) {

                if (response.isSuccessful) {


                    val body = response.body()


                    val tempNewsList = mutableListOf<Article>()


                    body?.articles?.forEach {


                        tempNewsList.add(
                            Article(
                                it.author,
                                it.content,
                                it.description,
                                it.publishedAt,
                                it.source,
                                it.title,
                                it.url,
                                it.urlToImage
                            )
                        )
                    }


                    newsList.value = tempNewsList


                } else {


                    val jsonObj: JSONObject?

                    try {
                        jsonObj = response.errorBody()?.string()?.let { JSONObject(it) }
                        if (jsonObj != null) {
                            var apiRequestError = true
                            var errorMessage = jsonObj.getString("message")
                            val tempNewsList = mutableListOf<Article>()
                            newsList.value = tempNewsList
                        }
                    } catch (e: JSONException) {
                        Log.d("JSONException", "" + e.message)
                    }

                }
            }

            override fun onFailure(call: Call<NewsData>, t: Throwable) {

                var apiRequestError = true
                var errorMessage = t.localizedMessage as String
                Log.d("err_msg", "msg" + t.localizedMessage)
            }
        })

        return newsList
    }

}


