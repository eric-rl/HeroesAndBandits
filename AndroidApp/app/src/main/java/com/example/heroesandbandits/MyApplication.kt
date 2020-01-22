package com.example.heroesandbandits

import android.app.Application
import android.content.Context

class MyApplication : Application() {
    companion object {
        lateinit var context: Context
        var searchForHeroes: Boolean = true
    }

    override fun onCreate() {
        super.onCreate()
        context = this

    }
}