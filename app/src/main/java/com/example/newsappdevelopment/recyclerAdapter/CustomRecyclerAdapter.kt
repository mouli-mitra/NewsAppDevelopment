package com.example.newsappdevelopment.recyclerAdapter


import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.newsappdevelopment.R
import com.example.newsappdevelopment.datamodel.Article
import com.example.newsappdevelopment.datamodel.NewsData
import com.squareup.picasso.Picasso
import java.time.Duration
import java.time.Instant
import java.time.ZoneId

class CustomRecyclerAdapter(private var newsList: List<Article>) :
    RecyclerView.Adapter<CustomRecyclerAdapter.ViewHolder>() {


    private lateinit var context: Context


    init {

        this.notifyDataSetChanged()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        context = parent.context
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val newsData = newsList[holder.adapterPosition]

        holder.headLine.text = newsData.title
        val time: String? = newsData.publishedAt
        val imgUrl = newsData.urlToImage

        if (imgUrl.isNullOrEmpty()) {

            Picasso.get()
                .load( R.drawable.samplenews)
                .fit()
                .centerCrop()
                .into(holder.image)
        } else {
            Picasso.get()
                .load(imgUrl)
                .fit()
                .centerCrop()
                .error(R.drawable.samplenews)
                .into(holder.image)
        }

        if (context.toString().contains(context.getString(R.string.savednews))) {
            val date = " " + time?.substring(0, time.indexOf('T', 0))
            holder.newsPublicationTime.text = date
        } else {
            val currentTimeInHours = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Instant.now().atZone(ZoneId.of("Asia/Kolkata"))
            } else {
                TODO("VERSION.SDK_INT < O")
            }
            val newsTimeInHours = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Instant.parse(time).atZone(ZoneId.of("Asia/Kolkata"))
            } else {
                TODO("VERSION.SDK_INT < O")
            }
            val hoursDifference = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Duration.between(currentTimeInHours, newsTimeInHours)
            } else {
                TODO("VERSION.SDK_INT < O")
            }
            val hoursAgo = " " + hoursDifference.toHours().toString().substring(1) + " hour ago"
            holder.newsPublicationTime.text = hoursAgo
        }

    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    class ViewHolder(
        ItemView: View

    ) : RecyclerView.ViewHolder(ItemView) {
        val image: ImageView = itemView.findViewById(R.id.img)
        val headLine: TextView = itemView.findViewById(R.id.news_title)
        val newsPublicationTime: TextView = itemView.findViewById(R.id.news_publication_time)




        }

    }

