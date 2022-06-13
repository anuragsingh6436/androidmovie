package com.example.android.dagger.dataBase

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovieBookMarkIdDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveBookMarkData(data:MovieBookMarkIdTable)

    @Query("SELECT * FROM movieBookMarktable")
    fun getBookMarkData():List<MovieBookMarkIdTable>?

    @Query("DELETE FROM movieBookMarktable where id = :id")
    fun deleteParticularId(id: Int)

    @Query("DELETE FROM movieBookMarktable")
    fun deleteAll()

    @Query("SELECT * FROM movieBookMarktable where id = :id")
    fun getParticularData(id: Int):MovieBookMarkIdTable?
}