package com.example.android.dagger.dataBase

import androidx.room.TypeConverter
import com.example.android.dagger.model.MovieListV2
import com.google.gson.Gson

class MovieNameTableConverter {

    @TypeConverter
    fun convertMovieNameTableIntoString(data:MovieListV2):String? {
        return Gson().toJson(data)
    }

    @TypeConverter
    fun convertStringIntoMovieList(value:String?):MovieListV2? {
        if(value == null) return null
        return Gson().fromJson(value,MovieListV2::class.java)
    }
}