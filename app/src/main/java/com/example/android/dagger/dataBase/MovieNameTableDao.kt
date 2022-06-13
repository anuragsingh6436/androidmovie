package com.example.android.dagger.dataBase

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovieNameTableDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveData(list: List<MovieNameTable>)

    @Query("SELECT * FROM movieListTable")
    fun getData(): List<MovieNameTable>

    @Query("SELECT * FROM movieListTable")
    fun selectAll(): Cursor

    @Query("DELETE FROM movieListTable")
    fun deleteAll()

    @Query("SELECT * FROM movieListTable where :id = id")
    fun getParticularId(id: Int): MovieNameTable

    @Query("UPDATE movieListTable SET isSelected = :isSelected where :id = id")
    fun toggleSelectionState(isSelected: Boolean, id: Int)
}