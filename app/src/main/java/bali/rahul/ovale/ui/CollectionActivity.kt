package bali.rahul.ovale.ui

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import bali.rahul.ovale.adapter.PhotoRecyclerAdapter
import bali.rahul.ovale.dataModel.Collection
import bali.rahul.ovale.dataModel.Photo
import bali.rahul.ovale.databinding.ActivityCollectionBinding
import bali.rahul.ovale.rest.RestApi
import bali.rahul.ovale.utils.Internet
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class CollectionActivity : AppCompatActivity() {


    private lateinit var xx: Disposable
    private lateinit var parcelCollection: Collection
    private var collectionPhotos: List<Photo> = listOf()
    private lateinit var binding: ActivityCollectionBinding

    private val adapter = PhotoRecyclerAdapter(collectionPhotos)


    private val tag = ">>>>>Collection"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCollectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Action bar connect with toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }


        // Retrieve the parcelable data from the intent
        parcelCollection = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("collection")!!
        } else {
            intent.getParcelableExtra("collection", Collection::class.java)!!
        }

        // Set the title of the activity
        supportActionBar?.title = parcelCollection.title

        // random photo fetch from unsplash
        if (Internet.isNetworkAvailable(this)) {
            //Here we create background task to fetch info
            xx = RestApi().retrofit.fetchCollectionPhotos(parcelCollection.id!!)
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
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        //Dispose the disposable object
        if (xx.isDisposed) {
            Log.d(tag, "Disposable object is disposed")
        } else {
            xx.dispose()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 101) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // storage permission granted
                Log.d(tag, "Storage permission granted")
            } else {
                // storage permission denied
                Log.d(tag, "Storage permission denied")
            }
        }
    }


    private fun handleError(error: Throwable) {
        Log.d(tag, error.message!!)
        Log.d(tag, error.stackTraceToString())
        Toast.makeText(this, error.localizedMessage, Toast.LENGTH_LONG).show()
    }

    private fun handleSuccess(photos: List<Photo>) {

        Log.d(tag, "CollectionPhotos: ${photos.size}")
        photos.forEach { Log.d(tag, "CollectionPhotos: ${it.id}") }

        //Set the layout manager for the recycler view
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        // set adapter to recyclerView
        binding.recyclerView.adapter = adapter

        // Add data to the adapter
        this.collectionPhotos = photos
        adapter.setPhotos(this.collectionPhotos)

    }


}