package org.sniffsnirr.skillcinema

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)//инициализация работы с jsr 310 работа со временем
    }

    companion object {
        const val POSTERS_DIR = "posters"
    }
}