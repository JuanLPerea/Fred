package com.fredzxgame

import android.app.Application

class SharedApp : Application() {

    companion object {
        lateinit var prefs: Prefs
        lateinit var myMediaPlayer: MyMediaPlayer
    }

    override fun onCreate() {
        super.onCreate()
        prefs = Prefs(applicationContext)
        myMediaPlayer = MyMediaPlayer(applicationContext)
    }
}