package org.sniffsnirr.skillcinema

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.transition.Visibility
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


import org.sniffsnirr.skillcinema.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var isFirstStart: Boolean = true
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = "skillcinema_settings",
        corruptionHandler = null,
        scope = CoroutineScope(Dispatchers.IO)
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        hideBars()


        this.lifecycleScope.launch {
            dataStore.data
                .collect { pref: Preferences ->
                    isFirstStart = pref[FIRST_START] ?: true
                    Log.d("DataStore","чтение isFirstStart=$isFirstStart")
                }
        }


        if (isFirstStart) {
            navController.navigate(R.id.action_startFragment_to_onboardingMainFragment)
            this.lifecycleScope.launch {
                dataStore.edit { prefs ->
                    prefs[FIRST_START] = false
                    Log.d("DataStore","запись isFirstStart=$isFirstStart")
                }
                dataStore.data
                    .collect { pref: Preferences ->
                        isFirstStart = pref[FIRST_START] ?: true
                        Log.d("DataStore","контрольное чтение isFirstStart=$isFirstStart")
                    }
            }
        }

    }

    fun hideBars() {
        binding.navView.visibility =
            View.GONE// скрываю элементы управления перед появлением loading экрана
        getSupportActionBar()?.hide()
    }

    fun showBars() {
        binding.navView.visibility =
            View.VISIBLE// показываю элементы управления
        getSupportActionBar()?.show()

    }

    companion object {
        val FIRST_START = booleanPreferencesKey("first_start")
    }
}