package org.sniffsnirr.skillcinema

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import org.sniffsnirr.skillcinema.App.Companion.POSTERS_DIR
import org.sniffsnirr.skillcinema.databinding.ActivityMainBinding
import org.sniffsnirr.skillcinema.ui.exception.BottomSheetErrorFragment

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment

        navController = navHostFragment.navController

        val navView: BottomNavigationView = binding.navView
        toolbar = binding.myToolbar

        setSupportActionBar(toolbar)

        navView.setupWithNavController(navController)

        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(false)

        toolbar.setNavigationIcon(R.drawable.action_bar_icon)

        toolbar.setNavigationOnClickListener { navController.popBackStack() }

        hideActionBar()//скрытие всех баров - они не нужны на фрагментах Onboard и loading
        hideButtomBar()

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

    private fun hideButtomBar() {
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
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}