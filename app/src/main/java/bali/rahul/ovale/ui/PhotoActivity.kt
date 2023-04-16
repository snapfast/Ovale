package bali.rahul.ovale.ui

import android.app.DownloadManager
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import bali.rahul.ovale.R
import bali.rahul.ovale.dataModel.Photo
import bali.rahul.ovale.databinding.ActivityPhotoBinding
import bali.rahul.ovale.service.OvaleWallpaperService
import bali.rahul.ovale.utils.Internet
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale


class PhotoActivity : AppCompatActivity() {

    private lateinit var parcelPhoto: Photo
    private lateinit var binding: ActivityPhotoBinding
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var inDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
    private var outDateFormat = SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.US)


    private val tag = "<<<<PhotoActivity>>>>"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        parcelPhoto = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("photo")!!
        } else {
            intent.getParcelableExtra("photo", Photo::class.java)!!
        }


        binding.toolbar.title = parcelPhoto.user?.name + " - " + parcelPhoto.user?.username

        Log.i(tag, "Received photo data: $parcelPhoto")

        val requestOptions = RequestOptions().placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)

        Glide.with(this).applyDefaultRequestOptions(requestOptions).load(parcelPhoto.urls?.regular)
            .transition(DrawableTransitionOptions.withCrossFade())
            .diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.imageView)
        binding.imageView.contentDescription = parcelPhoto.altDescription

        val dd = outDateFormat.format(inDateFormat.parse(parcelPhoto.createdAt!!)!!)

        // Set the text
        binding.textViewDescription.text = parcelPhoto.altDescription
        binding.textViewDate.text = dd.toString()
        binding.textViewLink.text = parcelPhoto.links?.html
        binding.textViewTitle.text = parcelPhoto.user?.name
        binding.textViewLocation.text =
            parcelPhoto.location?.city + ", " + parcelPhoto.location?.country
        try {
            binding.sharePhoto.setOnClickListener {
                val shareUrl = parcelPhoto.links?.html
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareUrl)
                startActivity(Intent.createChooser(shareIntent, "Share URL"))
            }
        } catch (e: Exception) {
            Log.e(tag, "Error: $e")
        }
        binding.textViewCamera.text = parcelPhoto.exif?.model
        binding.textViewFocalLength.text = parcelPhoto.exif?.focalLength
        binding.textViewAperture.text = parcelPhoto.exif?.aperture
        binding.textViewShutterSpeed.text = parcelPhoto.exif?.exposureTime
        binding.textViewISO.text = parcelPhoto.exif?.iso.toString()
        binding.textViewDimensions.text =
            parcelPhoto.width.toString() + " x " + parcelPhoto.height.toString()


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


