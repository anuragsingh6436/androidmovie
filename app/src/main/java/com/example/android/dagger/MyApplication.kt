

package com.example.android.dagger

import android.app.Application
import com.example.android.dagger.dataBase.Core
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
open class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Core.initialise(this)
    }
}

