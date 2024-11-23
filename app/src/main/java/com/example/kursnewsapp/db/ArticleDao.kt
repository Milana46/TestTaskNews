package com.example.kursnewsapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kursnewsapp.models.Article
import kotlinx.coroutines.flow.Flow


@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article:Article) :Long

    @Query("SELECT * FROM article")
     fun getAllArticle(): LiveData<List<Article>>

   @Query("SELECT COUNT(*) > 0 FROM article WHERE [key] = :articleId")
   suspend fun isArticleFavorite(articleId: Int): Boolean

    @Delete
    suspend fun deleteArticle(article:Article)
}
