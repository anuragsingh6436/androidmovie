package com.example.android.dagger.Network

import com.example.android.dagger.model.MovieList
import com.example.android.dagger.model.response.MovieDetailResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {

    @GET("popular?")
    suspend fun getusers(@Query("api_key")apiKey:String): MovieList

    @GET
    suspend fun getMovieDetail(@Url url:String): MovieDetailResponse
}