package com.example.newsappdevelopment

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsappdevelopment.datamodel.Article
import com.example.newsappdevelopment.recyclerAdapter.CustomRecyclerAdapter
import com.example.newsappdevelopment.repo.NewsViewModel

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: NewsViewModel
    lateinit var recyclerView: RecyclerView
    var newsData: MutableList<Article> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this)[NewsViewModel::class.java]

        recyclerView = this.findViewById(R.id.recyclerView)
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


        if (isNetworkAvailable(applicationContext)) {
            requestNews(getString(R.string.general), newsData)

        } else {
            Toast.makeText(this, getString(R.string.network), Toast.LENGTH_SHORT).show()
        }


    }

    private fun requestNews(s: String, newsData: MutableList<Article>) {


        viewModel.getNews(category = s, this)?.observe(this) {


            newsData.addAll(it)

            setRecyclerView()


        }


    }

    private fun setRecyclerView() {
        val adapter = CustomRecyclerAdapter(newsData)
        recyclerView.adapter = adapter


    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // For 29 api or above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                    ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            // For below 29 api
            if (connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnectedOrConnecting) {
                return true
            }
        }
        return false
    }
}
