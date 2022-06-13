package com.example.android.dagger.Network

class ApiHelper(private val apiService: ApiService) {

    suspend fun getUsers(apiKey:String) = apiService.getusers(apiKey = apiKey)
    suspend fun getMovieDetail(url:String) = apiService.getMovieDetail(url)
}