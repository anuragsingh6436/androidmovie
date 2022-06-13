package com.example.android.dagger.dataBase

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android.dagger.model.MovieListV2


@Entity(tableName = "movieBookMarktable")
data class MovieBookMarkIdTable(
    @PrimaryKey
    val id: Int,
    val title: String,
    val imageUrl:String,
    val overview:String,
    val releaseDate:String
)