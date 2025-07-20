package com.example.first_app.data.remote

import com.example.first_app.data.remote.dto.NewsResponse
import com.example.first_app.util.Constants.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("everything")
    suspend fun getNews(
        @Query("page") page: Int,      // read documentation to get idea of api
        @Query("sources") sources: String,
        @Query("apiKey") apiKey: String = API_KEY
    ):NewsResponse

    @GET("everything")
    suspend fun searchNews(
        @Query("q") searchQuery: String,
        @Query("sources") sources: String,
        @Query("page") page: Int,
        @Query("apiKey") apiKey: String = API_KEY
    ): NewsResponse
}