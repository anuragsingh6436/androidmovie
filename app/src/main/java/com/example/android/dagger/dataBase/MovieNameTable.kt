package com.example.android.dagger.dataBase

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android.dagger.model.MovieListV2

@Entity(tableName = "movieListTable")
data class MovieNameTable(
    @PrimaryKey
    val id: Int,
    val data: MovieListV2,
    val isSelected:Boolean
)