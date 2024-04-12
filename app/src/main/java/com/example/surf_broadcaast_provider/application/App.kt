package com.example.surf_broadcaast_provider.application

import android.app.Application
import android.util.Log

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        Log.d("App", "Вызвался метод onCreate()")
    }

}