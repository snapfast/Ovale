package bali.rahul.ovale.ui.home

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import bali.rahul.ovale.adapter.RecyclerAdapter
import bali.rahul.ovale.dataModel.Photo
import bali.rahul.ovale.databinding.FragmentHomeBinding
import bali.rahul.ovale.rest.RestApi
import bali.rahul.ovale.utils.Internet
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // declare list of Photos
    private var photos: MutableList<Photo> = mutableListOf()

    // declare adapter for Photos
    private val adapter = RecyclerAdapter(photos)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // random photo fetch from unsplash
        if (Internet().isNetworkAvailable(requireContext())) {
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
            Toast.makeText(this.context, "No Internet Connection", Toast.LENGTH_LONG).show()
        }
        return root
    }


//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }


    private fun handleError(error: Throwable) {
        Log.d(TAG, error.message!!)
        Log.d(TAG, error.stackTraceToString())
        Toast.makeText(this.context, error.localizedMessage, Toast.LENGTH_LONG).show()
    }

    private fun handleSuccess(photos: List<Photo>) {

        var imageSize = photos.size.toString()
        Log.d(TAG, imageSize)

        //Set the layout manager for the recycler view
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)

        // set adapter to recyclerView
        binding.recyclerView.adapter = adapter

        // Add data to the adapter
        this.photos = photos as MutableList<Photo>
        adapter.setPhotos(this.photos)

    }


}