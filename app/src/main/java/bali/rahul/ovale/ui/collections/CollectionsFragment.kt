package bali.rahul.ovale.ui.collections

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import bali.rahul.ovale.adapter.CollectionRecyclerAdapter
import bali.rahul.ovale.dataModel.Collection
import bali.rahul.ovale.databinding.FragmentCollectionsBinding
import bali.rahul.ovale.rest.RestApi
import bali.rahul.ovale.utils.Internet
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * A simple [Fragment] subclass.
 * Use the [CollectionsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CollectionsFragment : Fragment() {

    private var _binding: FragmentCollectionsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // declare list of Photos
    private var collections: List<Collection> = listOf()

    // declare adapter for Photos
    private val adapter = CollectionRecyclerAdapter(collections)

    // onCreateView
    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCollectionsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        if (Internet().isNetworkAvailable(requireContext())) {
            //Here we create background task to fetch info
            RestApi().retrofit.fetchCollections()
                //Everytime you use subscribe you switch to a worker thread
                .subscribeOn(Schedulers.io())
                //Observe on lets you get the data in the main thread by using android schedulers
                .observeOn(AndroidSchedulers.mainThread())
                //When you subscribe this is the time you can handle the error or success or the data
                //.subscribe(this::handleSuccess, this::handleError)
                .subscribe(
                    //Success with photo object
                    { collections -> handleSuccess(collections) },
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

    private fun handleSuccess(collectionList: List<Collection>) {

        //Set the layout manager for the recycler view
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)

        // set adapter to recyclerView
        binding.recyclerView.adapter = adapter

        // Add data to the adapter
        this.collections = collectionList
        adapter.setPhotos(this.collections)

    }

}