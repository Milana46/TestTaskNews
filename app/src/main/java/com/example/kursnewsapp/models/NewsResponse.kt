package com.example.kursnewsapp.models

data class NewsResponse(
    var articles: List<Article>,
    val status: String,
    val totalResults: Int
)