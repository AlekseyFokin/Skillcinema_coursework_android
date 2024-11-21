package org.sniffsnirr.skillcinema

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//val Context.dataStore: DataStore<Preferences> by preferencesDataStore( // работа с хранилищем DataStore
//    name = "skillcinema_settings",
//    corruptionHandler = null,
//    scope = CoroutineScope(Dispatchers.IO)
//)



@HiltAndroidApp
class App : Application() {
//    var isFirst = true
//
//    init {
//        with(ProcessLifecycleOwner.get()) {
//            lifecycleScope.launch(Dispatchers.IO) {
//                dataStore.data
//                    .collect { pref: Preferences ->
//                        isFirst = pref[FIRST_START] ?: true
//                    }
//            }.onJoin
//        }
//        Log.d("isFirst in App in init", "$isFirst")
//        with(ProcessLifecycleOwner.get()) {
//            lifecycleScope.launch {
//                dataStore.edit {
//                        prefs ->// сохранение метки о первой загрузке
//                    prefs[FIRST_START] = false
//                }
//            }.onJoin
//        }
//

  //  }


    override fun onCreate() {


        super.onCreate()
        AndroidThreeTen.init(this)//инициализация работы с jsr 310 работа со временем



    }


    companion object {
        const val POSTERS_DIR = "posters"

    }
}