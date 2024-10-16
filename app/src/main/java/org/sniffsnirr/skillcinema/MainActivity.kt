package org.sniffsnirr.skillcinema


import android.content.Context
import android.os.Bundle
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
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.sniffsnirr.skillcinema.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var navController: NavController? = null
    private lateinit var toolbar:Toolbar


    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore( // работа с хранилищем DataStore
        name = "skillcinema_settings",
        corruptionHandler = null,
        scope = CoroutineScope(Dispatchers.IO)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideActionBar()//скрытие всех баров - они не нужны на фрагментах Onboard и loading
        hideButtomBar()
        val navView: BottomNavigationView = binding.navView
        toolbar = findViewById(R.id.myToolbar)

        setSupportActionBar(toolbar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment

        navController = navHostFragment.navController
        navController!!.setGraph(R.navigation.mobile_navigation)

        navView.setupWithNavController(navController!!)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_profile
            )
        )

       //setupActionBarWithNavController(navController!!,appBarConfiguration)


        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(false)

         toolbar.setNavigationIcon(R.drawable.action_bar_icon)

        lifecycleScope.launch(Dispatchers.Main) { isFirstStart() }.onJoin

        toolbar.setNavigationOnClickListener { navController!!.popBackStack() }
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

    fun hideButtomBar() {
        binding.navView.visibility =
            View.GONE// скрываю ButtomBar
            }

    fun showButtomBar() {
        binding.navView.visibility =
            View.VISIBLE// показываю ButtomBar
            }

    fun hideActionBar() {//скрываю ToolBar
        //   getSupportActionBar()?.hide()
        binding.myToolbar.visibility=View.INVISIBLE
    }

    fun showActionBar() {//показываю ToolBar
       //     getSupportActionBar()?.show()
        binding.myToolbar.visibility=View.VISIBLE
    }

    fun setActionBarTitle(title:String){
           getSupportActionBar()?.title=title
    }

    override fun onSupportNavigateUp(): Boolean {
       // val navController = navController(R.id.navController)
        return navController!!.navigateUp() || super.onSupportNavigateUp()
    }


    companion object {
        val FIRST_START =
            booleanPreferencesKey("first_start")// ячейка для хранения метки о первой загрузке приложения в DataStore
    }

}