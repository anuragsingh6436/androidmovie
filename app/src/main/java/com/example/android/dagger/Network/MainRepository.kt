package com.example.android.dagger.Network

import com.example.android.dagger.model.MovieList
import com.example.android.dagger.model.response.MovieDetailResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MainRepository (private val apiHelper: ApiHelper) {

    suspend fun getUsers(apiKey:String): Flow<MovieList> {
        return flow{
            emit(apiHelper.getUsers(apiKey))
        }
    }

    suspend fun getMovieDetail(url:String):Flow<MovieDetailResponse> {
        return flow {
            emit(apiHelper.getMovieDetail(url))
        }
    }
}