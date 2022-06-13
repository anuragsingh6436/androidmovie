package com.example.android.dagger.dataBase

object DataBaseRepository {

    val dataBase = MovieDataBase.getInstance()

    fun saveMovieData(list: List<MovieNameTable>) {
        dataBase.getMovieNameDao().saveData(list)
    }

    fun getMovieData(): List<MovieNameTable> {
        return dataBase.getMovieNameDao().getData()
    }

    fun deleteAllMovieDataFromDb() {
        dataBase.getMovieNameDao().deleteAll()
    }

    fun getBookMarkData(id:Int):MovieBookMarkIdTable? {
        return dataBase.getMovieBookMarkDao().getParticularData(id)
    }

    fun saveBookMarkData(data:MovieBookMarkIdTable){
        dataBase.getMovieBookMarkDao().saveBookMarkData(data)
    }

    fun getAllBookMarkItems():List<MovieBookMarkIdTable>? {
        return dataBase.getMovieBookMarkDao().getBookMarkData()
    }

    fun deleteParticularBookMarkekItem(id:Int) {
        return dataBase.getMovieBookMarkDao().deleteParticularId(id)
    }
}