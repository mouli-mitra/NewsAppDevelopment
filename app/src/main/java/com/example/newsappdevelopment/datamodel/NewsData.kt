package com.example.newsappdevelopment.datamodel

data class NewsData(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)