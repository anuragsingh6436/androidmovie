package com.example.android.dagger.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.example.android.dagger.dataBase.MovieDataBase
import java.lang.IllegalArgumentException

class MovieContentProvider : ContentProvider() {

    companion object {
        const val AUTORITY_URI = "authority_uri"
        const val TABLE_NAME = "movieListTable"
        const val ID = 1
        val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
        uriMatcher.addURI(AUTORITY_URI, TABLE_NAME, ID)
        }
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        Log.d("tag6","here")
       val code = uriMatcher.match(uri)
        if(code == ID) {
            val context = context ?: return null
            val dao = MovieDataBase.getInstance().getMovieNameDao()
            return dao.selectAll()
        } else {
            throw IllegalArgumentException("unknown uri${uri}")
        }
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        return 0
    }
}