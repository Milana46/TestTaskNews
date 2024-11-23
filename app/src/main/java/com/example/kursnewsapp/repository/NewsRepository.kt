package com.example.kursnewsapp.repository

import com.example.kursnewsapp.api.RetrofitInstance
import com.example.kursnewsapp.db.DataBaseArticle
import com.example.kursnewsapp.models.Article


class NewsRepository(private val db: DataBaseArticle) {

    suspend fun getHeadlines(countryCode:String, pageNumber: Int)=RetrofitInstance
        .api.getHeadlines(countryCode,pageNumber)
    suspend fun insertArticle(article: Article)=db.dao().insertArticle(article)


    suspend fun isArticleFavorite(articleId: Int): Boolean {
        return db.dao().isArticleFavorite(articleId)
    }

    fun getAllArticle()=db.dao().getAllArticle()

    suspend fun deleteArticle(article:Article)=db.dao().deleteArticle(article)
}
