//package com.example.android.dagger.sharedPrefernces
//
//import android.content.Context
//import com.google.gson.Gson
//
//class SharedPrefencesUtil {
//
//    companion object {
//        const val MOVIE_ID_ARRAY = "MOVIE_ID_ARRAY"
//    }
//
//    fun getFileName():String {
//        return "my_pref"
//    }
//
//    fun saveIdIntoArrayList(context: Context,id:String) {
//        val sharedPrefs = context.getSharedPreferences(getFileName(),Context.MODE_PRIVATE)
//        val editor = sharedPrefs.edit()
//
//        val list = sharedPrefs.getString(MOVIE_ID_ARRAY,null)
//        if(list == null) {
//            val movieArrayList:ArrayList<String>  = ObjectSerializer.serialize(list) as ArrayList<String> /* = java.util.ArrayList<kotlin.String> */
//            movieArrayList.add(id)
//            editor.putString(MOVIE_ID_ARRAY,Gson().toJson(movieArrayList))
//        } else {
//            val movieArraylist = Gson().fromJson<ArrayList<String>>()
//        }
//    }
//
//    fun getIdArrayFromSharedPreferences() {
//
//    }
//}