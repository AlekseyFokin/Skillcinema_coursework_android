package org.sniffsnirr.skillcinema


import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.sniffsnirr.skillcinema.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var navController: NavController? = null

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore( // работа с хранилищем DataStore
        name = "skillcinema_settings",
        corruptionHandler = null,
        scope = CoroutineScope(Dispatchers.IO)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_profile
            )
        )
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.navController

        setupActionBarWithNavController(navController!!, appBarConfiguration)
        navView.setupWithNavController(navController!!)

        hideBars()//скрытие всех баров - они не нужны на фрагментах Onboard и loading

        lifecycleScope.launch(Dispatchers.Main) { isFirstStart() }.onJoin
    }

    private suspend fun isFirstStart() {
        val graph = navController!!.navInflater.inflate(R.navigation.mobile_navigation)
        var isFirstStart = true // по умолчанию - первая загрузка
        lifecycleScope.launch {
            dataStore.data
                .collect { pref: Preferences ->
                    isFirstStart = pref[FIRST_START]
                        ?: true // чтение из DataStore метки первого открытия приложения
                    if (isFirstStart) {  // если в DataStore нет метки о первой загрузке - переход на onboarding фрагмент
                        graph.setStartDestination(R.id.onboardingMainFragment)
                        navController!!.graph = graph
                        dataStore.edit { prefs ->// сохранение метки о первой загрузке
                            prefs[FIRST_START] = false
                        }
                    } else { // загрузка не первая - переход в фрагмент loadingFragment
                        graph.setStartDestination(R.id.loadingFragment)
                    }
                    navController!!.graph = graph
                }
        }
    }

    fun hideBars() {
        binding.navView.visibility =
            View.GONE// скрываю элементы управления
        getSupportActionBar()?.hide()
    }

    fun showBars() {
        binding.navView.visibility =
            View.VISIBLE// показываю элементы управления
        getSupportActionBar()?.show()
    }

    companion object {
        val FIRST_START =
            booleanPreferencesKey("first_start")// ячейка для хранения метки о первой загрузке приложения в DataStore
    }

}