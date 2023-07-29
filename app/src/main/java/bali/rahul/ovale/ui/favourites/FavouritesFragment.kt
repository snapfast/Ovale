package bali.rahul.ovale.ui.favourites

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import bali.rahul.ovale.adapter.PhotoStoreRecyclerAdapter
import bali.rahul.ovale.databinding.FragmentFavouritesBinding
import bali.rahul.ovale.storage.OvaleDatabase
import bali.rahul.ovale.storage.model.PhotoStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass.
 * Use the [FavouritesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavouritesFragment : Fragment() {

    private var _binding: FragmentFavouritesBinding? = null

    // Define a coroutine scope
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var photoStoreList = listOf<PhotoStore>()

    // declare adapter for Photos
    private val adapter = PhotoStoreRecyclerAdapter(photoStoreList)

    // Create a suspend function that calls the DAO's read function inside a coroutine
    private suspend fun readItem(itemView: View) {
        withContext(Dispatchers.IO) {
            val db = OvaleDatabase.getInstance(itemView.context)
            val photoDao = db.photoDao() // initialized photoDao
            photoStoreList = photoDao.getAllPhotos()
        }
    }


    // onCreateView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        val root: View = binding.root


        // write data from db to textview
        coroutineScope.launch {
            readItem(root)

            Toast.makeText(context, photoStoreList.toString(), Toast.LENGTH_SHORT).show()

            //Set the layout manager for the recycler view
            binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

            // set adapter to recyclerview
            binding.recyclerView.adapter = adapter

            Log.d(">>>>>>>>>>>>>>", "onViewCreated: $photoStoreList")
            adapter.setPhotoStores(photoStoreList)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}