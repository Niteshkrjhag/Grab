package com.example.first_app.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.first_app.domain.model.Article
import kotlinx.coroutines.flow.Flow


@Dao   // performing CRUD operations on the database
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article)

    @Delete
    suspend fun delete(article: Article)

    @Query("SELECT * FROM Article")
    fun getArticles(): Flow<List<Article>>

    @Query("SELECT * FROM Article WHERE url=:url")
    suspend fun getArticle(url: String): Article?

}