package bali.rahul.ovale

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import bali.rahul.ovale.databinding.ActivityMainBinding
import bali.rahul.ovale.ui.SettingsActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.color.DynamicColors
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController


    private val TAG = ">>>>>>>>>>>>>MAIN ACTIVITY"

    override fun onCreate(savedInstanceState: Bundle?) {

        // Make the status bar and navigation bar dynamic colors using Material 3 Components
        // WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        // Set the theme to the one selected by the user
        DynamicColors.applyToActivitiesIfAvailable(application)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Action bar connect with toolbar
        setSupportActionBar(binding.toolbar)

        // Bottom navigation view
        val navView: BottomNavigationView = binding.navView

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_collections, R.id.navigation_favourites
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)
        Log.i(TAG, "onCreateOptionsMenu")
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 101) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // storage permission granted
                Log.d(TAG, "Storage permission granted")
            } else {
                // storage permission denied
                Log.d(TAG, "Storage permission denied")
            }
        }
    }


}