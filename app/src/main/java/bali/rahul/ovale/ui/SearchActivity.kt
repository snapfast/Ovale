package bali.rahul.ovale.ui

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.recyclerview.widget.LinearLayoutManager
import bali.rahul.ovale.adapter.PhotoRecyclerAdapter
import bali.rahul.ovale.dataModel.Photo
import bali.rahul.ovale.dataModel.SearchResult
import bali.rahul.ovale.databinding.ActivitySearchBinding
import bali.rahul.ovale.rest.RestApi
import bali.rahul.ovale.utils.Internet
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers


class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    // declare list of Photos
    private var photos: MutableList<Photo> = mutableListOf()

    // declare adapter for Photos
    private val adapter = PhotoRecyclerAdapter(photos)

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.searchBar)
        // on close icon click
        binding.searchBar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        // Search view on toolbar
        val searchView = binding.searchView
        searchView.editText.setOnEditorActionListener (OnEditorActionListener {v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchView.editText.clearFocus()
                binding.searchBar.text = searchView.text;
                searchView.hide();
                searchPhoto(v.text.toString().trim())
                return@OnEditorActionListener true
            }
            false
        })

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
