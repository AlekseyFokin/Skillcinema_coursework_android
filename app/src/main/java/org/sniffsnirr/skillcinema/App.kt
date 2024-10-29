package org.sniffsnirr.skillcinema

import android.app.Application
import android.content.Context
import android.util.Log
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.HiltAndroidApp
import java.io.File

@HiltAndroidApp
class App: Application(){
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)//инициализация работы с jsr 310 работа со временем
        try {
            getDir(POSTERS_DIR, Context.MODE_PRIVATE)
        }catch(e: Exception){
            Log.d("Create directory error","coud'not create dir for posters")
        }
    }
    companion object{
        const val POSTERS_DIR="posters"
    }
}