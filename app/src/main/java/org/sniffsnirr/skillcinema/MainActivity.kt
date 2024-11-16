package org.sniffsnirr.skillcinema

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavGraphNavigator
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.sniffsnirr.skillcinema.App.Companion.POSTERS_DIR
import org.sniffsnirr.skillcinema.databinding.ActivityMainBinding
import org.sniffsnirr.skillcinema.ui.exception.BottomSheetErrorFragment

val Context.dataStore: DataStore<Preferences> by preferencesDataStore( // работа с хранилищем DataStore
    name = "skillcinema_settings",
    corruptionHandler = null,
    scope = CoroutineScope(Dispatchers.IO)
)

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController//? = null
    private lateinit var toolbar: Toolbar
    private lateinit var graph: NavGraph


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment

        navController = navHostFragment.navController

        val inflater = navController.navInflater
        graph = inflater.inflate(R.navigation.mobile_navigation)

        lifecycleScope.launch(Dispatchers.Main) { isFirstStart() }.onJoin

        hideActionBar()//скрытие всех баров - они не нужны на фрагментах Onboard и loading
        hideButtomBar()
        val navView: BottomNavigationView = binding.navView
        toolbar = findViewById(R.id.myToolbar)

        setSupportActionBar(toolbar)

        navView.setupWithNavController(navController!!)

        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(false)

        toolbar.setNavigationIcon(R.drawable.action_bar_icon)

        toolbar.setNavigationOnClickListener { navController!!.popBackStack() }

        try {
            getDir(
                POSTERS_DIR,
                Context.MODE_PRIVATE
            )// создание папки для хранения файлов с постерами фмльмов
        } catch (e: Exception) {
            Log.d("Create directory error", "coud'not create dir for posters")
            BottomSheetErrorFragment().show(supportFragmentManager, "errordialog")
        }
    }

    private fun isFirstStart() {
        var isFirstStart = true // по умолчанию - первая загрузка
        lifecycleScope.launch {
            dataStore.data
                .collect { pref: Preferences ->
                    isFirstStart = pref[FIRST_START]
                        ?: true // чтение из DataStore метки первого открытия приложения
                    if (isFirstStart) {  // если в DataStore нет метки о первой загрузке - переход на onboarding фрагмент
                        navController.graph =
                            graph.apply { setStartDestination(R.id.onboardingMainFragment) }
                        dataStore.edit { prefs ->// сохранение метки о первой загрузке
                            prefs[FIRST_START] = false
                        }
                    } else { // загрузка не первая - переход в фрагмент loadingFragment
                        navController.graph =
                            graph.apply { setStartDestination(R.id.loadingFragment) }
                    }
                }
        }
    }

    fun hideButtomBar() {
        binding.navView.visibility =
            View.GONE// скрываю ButtomBar
    }

    fun showButtomBar() {
        binding.navView.visibility =
            View.VISIBLE// показываю ButtomBar
    }

    fun hideActionBar() {//скрываю ToolBar
        binding.myToolbar.visibility = View.INVISIBLE
    }

    fun showActionBar() {//показываю ToolBar
        binding.myToolbar.visibility = View.VISIBLE
    }

    fun setActionBarTitle(title: String) {
        getSupportActionBar()?.title = title
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController!!.navigateUp() || super.onSupportNavigateUp()
    }


    companion object {
        val FIRST_START =
            booleanPreferencesKey("first_start")// ячейка для хранения метки о первой загрузке приложения в DataStore

    }

}