package com.example.android.dagger

import android.util.Log

class asrClass(): i1,i2{
     init {
         //Log.d("tag4",a.toString())
     }

    override fun a() {
        TODO("Not yet implemented")
    }

}

interface i1 {
    val a: Int
        get() = 10

    fun a() {
        Log.d("tag4", a.toString())
    }
}

interface i2 {
    fun a() {
        Log.d("tag4","asr")
    }
}