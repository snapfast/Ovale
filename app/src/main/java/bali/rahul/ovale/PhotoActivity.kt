package bali.rahul.ovale

//import androidx.core.content.IntentCompat
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import bali.rahul.ovale.dataModel.Photo
import bali.rahul.ovale.databinding.ActivityPhotoBinding
import com.bumptech.glide.Glide

class PhotoActivity : AppCompatActivity() {

    val TAG = "<<<<<<<<<<<<PhotoActivity>>>>>>>>>>>>"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val parcelPhoto: Photo?

        val photoImage: ImageView = findViewById(R.id.imageView)

        // Set the toolbar with Back button
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }


        // Retrieve the parcelable data from the intent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            parcelPhoto = intent.getParcelableExtra<Photo>("photo", Photo::class.java)
        } else {
            parcelPhoto = intent.getParcelableExtra<Photo>("photo")
        }

        if (parcelPhoto == null) {
            Log.e(TAG, "No photo data received!")
        } else {
            Log.i(TAG, "Received photo data: $parcelPhoto")
        }

        // Set the image using Glide
        if (parcelPhoto != null) {

            Log.d(TAG, "Received photo data: $parcelPhoto")


            Glide.with(this).load(parcelPhoto.urls?.regular)
                .placeholder(R.drawable.ic_launcher_background).into(photoImage)

            // Set the text
            binding.descriptionTextView.text = parcelPhoto.description
            binding.dateTextView.text = parcelPhoto.createdAt
            binding.linkTextView.text = parcelPhoto.links?.html
        } else {
            binding.descriptionTextView.text = "No photo data received!"
            Toast.makeText(this, "No photo data received!", Toast.LENGTH_SHORT).show()
        }
    }
}

