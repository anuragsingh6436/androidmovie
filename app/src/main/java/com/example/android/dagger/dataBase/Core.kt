package com.example.android.dagger.dataBase

import android.content.Context

class Core {

    companion object {
        private const val TAG = "MMTCore"
        lateinit var mContext: Context

        @JvmStatic
        fun initialise(context: Context) {
            mContext = context
        }
    }
}