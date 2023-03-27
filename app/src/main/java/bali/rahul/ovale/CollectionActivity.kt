package bali.rahul.ovale

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import bali.rahul.ovale.adapter.RecyclerAdapter
import bali.rahul.ovale.dataModel.Photo
import bali.rahul.ovale.databinding.ActivityCollectionBinding
import bali.rahul.ovale.rest.RestApi
import bali.rahul.ovale.utils.Internet
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers


class CollectionActivity : AppCompatActivity() {

    private val TAG = ">>>>>>>>>>>>>TAG hai bencho"

    private lateinit var binding: ActivityCollectionBinding
    private var photos: MutableList<Photo> = mutableListOf()

    // declare adapter for Photos
    private val adapter = RecyclerAdapter(photos)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCollectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "kuch toh ho rha hai")

        Log.d(TAG, "net toh chal rha hai")

        val internet = Internet()

        if (Internet().isNetworkAvailable(applicationContext)) {
            //Here we create background task to fetch info
            RestApi().retrofit.fetchCollectionPhotos("nB1aKrGZ0XQ")
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

