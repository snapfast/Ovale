package bali.rahul.ovale

import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import bali.rahul.ovale.adapter.PhotoRecyclerAdapter
import bali.rahul.ovale.adapter.ViewPager2Adapter
import bali.rahul.ovale.dataModel.Photo
import bali.rahul.ovale.dataModel.SearchResult
import bali.rahul.ovale.databinding.ActivityMainBinding
import bali.rahul.ovale.rest.RestApi
import bali.rahul.ovale.ui.SearchActivity
import bali.rahul.ovale.ui.SettingsActivity
import bali.rahul.ovale.ui.collections.CollectionsFragment
import bali.rahul.ovale.ui.favourites.FavouritesFragment
import bali.rahul.ovale.ui.home.HomeFragment
import bali.rahul.ovale.utils.Internet
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // declare list of Photos
    private var photos: MutableList<Photo> = mutableListOf()

    // declare adapter for Photos
    private val adapter = PhotoRecyclerAdapter(photos)

    private val TAG = ">>>>>>MAIN ACTIVITY"

    override fun onCreate(savedInstanceState: Bundle?) {

        // Make the status bar and navigation bar dynamic colors using Material 3 Components
        // WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Action bar connect with toolbar
        setSupportActionBar(binding.searchBar)

        val viewPagerMain: ViewPager2 = binding.viewPager2
        val navView: BottomNavigationView = binding.navView
        val hf: HomeFragment = HomeFragment()
        val cf: CollectionsFragment = CollectionsFragment()
        val ff: FavouritesFragment = FavouritesFragment()
        val fragmentArray: Array<Fragment> = arrayOf<Fragment>(hf, cf, ff)

        val viewPager2Adapter: ViewPager2Adapter = ViewPager2Adapter(this, fragmentArray)
        // set Adapter
        viewPagerMain.adapter = viewPager2Adapter

        // change icons on swipe
        viewPagerMain.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> navView.selectedItemId = R.id.navigation_home
                    1 -> navView.selectedItemId = R.id.navigation_collections
                    2 -> navView.selectedItemId = R.id.navigation_favourites
                }
            }
        })

        // switch on the nav to change pages
        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> viewPagerMain.currentItem = 0
                R.id.navigation_collections -> viewPagerMain.currentItem = 1
                R.id.navigation_favourites -> viewPagerMain.currentItem = 2
            }
            true
        }

        //
        // Search view on toolbar
        val searchView = binding.searchView
        searchView.editText.setOnEditorActionListener (OnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchView.editText.clearFocus()
                binding.searchBar.text = searchView.text;
                searchView.hide();
                searchPhoto(v.text.toString().trim())
                return@OnEditorActionListener true
            }
            false
        })

        binding.fab.setOnClickListener {
            intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
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

    private fun searchPhoto(query: String) {

        if (Internet.isNetworkAvailable(this.baseContext)) {
            //Here we create background task to fetch info
            val xx = RestApi().retrofit.fetchSearchPhotos(query, 1, 30)
                //Everytime you use subscribe you switch to a worker thread
                .subscribeOn(Schedulers.io())
                //Observe on lets you get the data in the main thread by using android schedulers
                .observeOn(AndroidSchedulers.from(mainLooper, true))
                //When you subscribe this is the time you can handle the error or success or the data
                .subscribe(
                    //Success with photo object
                    { sr -> handleSuccess(sr) },
                    //Error with throwable object
                    { error -> handleError(error) })

        } else {
            Toast.makeText(baseContext, "No Internet Connection", Toast.LENGTH_LONG).show()
        }

    }

    private fun handleError(error: Throwable) {
        Log.d(ContentValues.TAG, error.message!!)
        Log.d(ContentValues.TAG, error.stackTraceToString())
        Toast.makeText(baseContext, error.localizedMessage, Toast.LENGTH_LONG).show()
    }

    private fun handleSuccess(searchResult: SearchResult) {

        Log.d("hgjg", searchResult.toString())

        photos = searchResult.results

        //Set the layout manager for the recycler view
        binding.recyclerView.layoutManager =
            LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)

        // set adapter to recyclerView
        binding.recyclerView.adapter = adapter

        // Add data to the adapter
        adapter.setPhotos(photos)

    }



}

