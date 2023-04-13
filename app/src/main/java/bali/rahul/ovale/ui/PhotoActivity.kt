package bali.rahul.ovale.ui

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import bali.rahul.ovale.R
import bali.rahul.ovale.dataModel.Photo
import bali.rahul.ovale.databinding.ActivityPhotoBinding
import bali.rahul.ovale.service.OvaleWallpaperService
import bali.rahul.ovale.utils.Internet
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class PhotoActivity : AppCompatActivity() {


    private lateinit var parcelPhoto: Photo
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    var dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")

    private val tag = "<<<<PhotoActivity>>>>"

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val photoImage: ImageView = findViewById(R.id.imageView)

        // Set the toolbar with Back button
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        // check parcelable object key is not found
        if (!intent.hasExtra("photo")) {
            Log.e(tag, "No photo data received")
            Toast.makeText(this, "No photo data received", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Retrieve the parcelable data from the intent
        parcelPhoto = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            // this is for API 29 and below
            intent.getParcelableExtra("photo")!!
        } else {
            // this is for API 30 and above
            intent.getParcelableExtra("photo", Photo::class.java)!!
        }

        Log.i(tag, "Received photo data: $parcelPhoto")

        // Set the image using Glide

        Log.d(tag, "Received photo data: $parcelPhoto")

        val requestOptions = RequestOptions().placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)

        Glide.with(this).applyDefaultRequestOptions(requestOptions).load(parcelPhoto.urls?.regular)
            .transition(DrawableTransitionOptions.withCrossFade())
            .diskCacheStrategy(com.bumptech.glide.load.engine.DiskCacheStrategy.ALL)
            .into(photoImage)
        photoImage.contentDescription = parcelPhoto.altDescription

        val dd =
            SimpleDateFormat("EEEE, MMMM dd, yyyy").format(dateFormat.parse(parcelPhoto.createdAt!!)!!)

        // Set the text
        binding.descriptionTextView.text = parcelPhoto.altDescription
        binding.dateTextView.text = dd.toString()
        binding.linkTextView.text = parcelPhoto.links?.html
        binding.toolbar.title = parcelPhoto.user?.name + " - " + parcelPhoto.user?.username

        // On Click Download Button, download the image using Glide to new_folder
        binding.downloadPhoto.setOnClickListener {

            // download photo
            coroutineScope.launch { downloadPhoto(parcelPhoto) }

        }

        binding.setWallpaper.setOnClickListener {
            OvaleWallpaperService.setAsWallpaper(parcelPhoto.urls?.regular!!, this)
        }
    }

    private fun downloadPhoto(parcelPhoto: Photo) {

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager

        val request = DownloadManager.Request(Uri.parse(parcelPhoto.urls?.full)).apply {
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
            setTitle("Downloading Photo from ${parcelPhoto.user?.name}.jpg")
            setDescription("Downloading an image from ${parcelPhoto.links?.html}")
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setDestinationInExternalPublicDir(
                Environment.DIRECTORY_PICTURES,
                "Ovale/${parcelPhoto.user?.username}/${parcelPhoto.id}.jpg"
            )
        }
        downloadManager.enqueue(request)
        if (Internet.isNetworkAvailable(this)) {
            Toast.makeText(this, "Downloading started...", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "No Internet, Downloading paused...", Toast.LENGTH_SHORT).show()
        }
    }

}


