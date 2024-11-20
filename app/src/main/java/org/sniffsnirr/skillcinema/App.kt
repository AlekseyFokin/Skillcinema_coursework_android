package org.sniffsnirr.skillcinema

import android.app.Application
import android.content.Context
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

val Context.dataStore: DataStore<Preferences> by preferencesDataStore( // работа с хранилищем DataStore
    name = "skillcinema_settings",
    corruptionHandler = null,
    scope = CoroutineScope(Dispatchers.IO)
)

@HiltAndroidApp
class App: Application(){

     var isFirst=true


    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)//инициализация работы с jsr 310 работа со временем


        with(ProcessLifecycleOwner.get()) {
            lifecycleScope.launch {
                dataStore.data
                    .collect { pref: Preferences ->
                        isFirst = pref[FIRST_START]
                            ?: true // чтение из DataStore метки первого открытия приложения
                        if (isFirst) {  // если в DataStore нет метки о первой загрузке - переход на onboarding фрагмент
                            dataStore.edit { prefs ->// сохранение метки о первой загрузке
                                prefs[FIRST_START] = false
                            }
                        } else { // загрузка не первая - переход в фрагмент loadingFragment
                            isFirst=false

                        }
                    }
            }
        }

    }


    companion object{
        const val POSTERS_DIR="posters"
        val FIRST_START =
            booleanPreferencesKey("first_start")// ячейка для хранения метки о первой загрузке приложения в DataStore
    }
}