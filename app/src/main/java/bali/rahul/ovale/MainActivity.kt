package bali.rahul.ovale

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.recyclerview.widget.LinearLayoutManager
import bali.rahul.ovale.adapter.RecyclerAdapter
import bali.rahul.ovale.dataModel.Photo
import bali.rahul.ovale.databinding.ActivityMainBinding
import bali.rahul.ovale.rest.RestApi
import bali.rahul.ovale.utils.Internet
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    private var photos: MutableList<Photo> = mutableListOf()
    private lateinit var binding: ActivityMainBinding

    // declare adapter for Photos
    private val adapter = RecyclerAdapter(photos)

    private val TAG = ">>>>>>>>>>>>>MAIN ACTIVITY"

    override fun onCreate(savedInstanceState: Bundle?) {

        // Make the status bar and navigation bar dynamic colors using Material 3 Components
        // DynamicColors.applyToActivitiesIfAvailable(application);
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)


        binding.buttonPhoto.setOnClickListener {
            // intent can be done from activity only, so "activity" is passed instead of the "this".
            val intent = Intent(baseContext, CollectionActivity::class.java)
            startActivity(intent)
        }

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }


        if (Internet().isNetworkAvailable(applicationContext)) {
            //Here we create background task to fetch info
            RestApi().retrofit.fetchRandomPhoto()
                //Everytime you use subscribe you switch to a worker thread
                .subscribeOn(Schedulers.io())
                //Observe on lets you get the data in the main thread by using android schedulers
                .observeOn(AndroidSchedulers.mainThread())
                //When you subscribe this is the time you can handle the error or success or the data
                //.subscribe(this::handleSuccess, this::handleError)
                .subscribe(
                    //Success with photo object
                    { photos -> handleSuccess(photos) },
                    //Error with throwable object
                    { error -> handleError(error) })

        } else {
            Toast.makeText(baseContext, "No Internet Connection", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)
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


    private fun handleError(error: Throwable) {
        Log.d(TAG, error.message!!)
        Log.d(TAG, error.stackTraceToString())
        Toast.makeText(baseContext, error.localizedMessage, Toast.LENGTH_LONG).show()
    }

    private fun handleSuccess(photos: List<Photo>) {

        var imageSize = photos.size.toString()
        Log.d(TAG, imageSize)

        //Set the layout manager for the recycler view
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        // set adapter to recyclerView
        binding.recyclerView.adapter = adapter

        // Add data to the adapter
        this.photos = photos as MutableList<Photo>
        adapter.setPhotos(this.photos)

    }

}