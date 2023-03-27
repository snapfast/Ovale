package bali.rahul.ovale

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import bali.rahul.ovale.databinding.ActivityFavouritesBinding
import bali.rahul.ovale.storage.repository.PhotoStoreRepository
import kotlinx.coroutines.launch

class FavouritesActivity : AppCompatActivity() {

    private val TAG = "FavouritesActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityFavouritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // read PhotoStore from database
        lifecycleScope.launch {
            val data = PhotoStoreRepository(this@FavouritesActivity).getAllPhotos()
            Log.d(TAG, "onCreate: $data")

            binding.textView.text = data.toString()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


}