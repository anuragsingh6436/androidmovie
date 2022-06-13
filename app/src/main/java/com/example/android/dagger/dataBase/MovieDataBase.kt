package com.example.android.dagger.dataBase

import android.content.Context
import androidx.room.*

@Database(entities = [MovieNameTable::class, MovieBookMarkIdTable::class], version = 1)
@TypeConverters(MovieNameTableConverter::class)
abstract class MovieDataBase : RoomDatabase() {

    abstract fun getMovieNameDao(): MovieNameTableDao

    abstract fun getMovieBookMarkDao(): MovieBookMarkIdDao

    companion object {
        @Volatile
        private var INSTANCE: MovieDataBase? = null

        @JvmStatic
        fun getInstance(): MovieDataBase {
            val tempInstnace = INSTANCE
            if (tempInstnace != null) return tempInstnace
            synchronized(this) {
                val instance =
                    Room.databaseBuilder(Core.mContext, MovieDataBase::class.java, "movie_database")
                        .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}