package bali.rahul.ovale.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import bali.rahul.ovale.R
import bali.rahul.ovale.dataModel.Photo
import bali.rahul.ovale.databinding.ActivityPhotoBinding
import bali.rahul.ovale.service.DownloadService
import bali.rahul.ovale.service.OvaleWallpaperService
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
        supportActionBar?.setDisplayShowHomeEnabled(true)
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

        Log.i(tag, "Received photo data: $parcelPhoto")

        supportActionBar?.title = parcelPhoto.user?.name + " - " + parcelPhoto.user?.username

        val requestOptions = RequestOptions().placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)

        // Set the image
        Glide.with(this).applyDefaultRequestOptions(requestOptions).load(parcelPhoto.urls?.regular)
            .transition(DrawableTransitionOptions.withCrossFade())
            .diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.imageView)
        binding.imageView.contentDescription = parcelPhoto.altDescription

        // Set the text
        binding.textViewDescription.text = parcelPhoto.altDescription
        binding.textViewDate.text = dateConverter(parcelPhoto.createdAt!!)
        binding.textViewLink.text = parcelPhoto.links?.html
        binding.textViewTitle.text = parcelPhoto.user?.name
        binding.textViewLocation.text = parcelPhoto.user?.location
        binding.textViewCamera.text = makeCameraString()
        binding.textViewFocalLength.text = parcelPhoto.exif?.focalLength
        binding.textViewAperture.text = parcelPhoto.exif?.aperture
        binding.textViewShutterSpeed.text = parcelPhoto.exif?.exposureTime
        binding.textViewISO.text = parcelPhoto.exif?.iso.toString()
        binding.textViewResolution.text = makeResolutionString()

        // On Click Download Button, download the image using service
        binding.downloadPhoto.setOnClickListener {
            // download photo
            coroutineScope.launch { DownloadService(baseContext).downloadPhoto(parcelPhoto) }
        }

        // On Click Set Wallpaper Button, set the image as wallpaper
        binding.setWallpaper.setOnClickListener {
            OvaleWallpaperService.setAsWallpaper(parcelPhoto.urls?.full!!, this)
        }

        // On Click Share Button, share the image url
        try {
            binding.sharePhoto.setOnClickListener {
                sharePhoto()
            }
        } catch (e: Exception) {
            Log.e(tag, "Error: $e")
        }
    }

    private fun dateConverter(date: String): String {
        val dd = inDateFormat.parse(date)
        return outDateFormat.format(dd!!).toString()
    }

    private fun makeResolutionString(): String {
        return "${parcelPhoto.width!!} x ${parcelPhoto.height!!}"
    }

    private fun makeCameraString(): String {
        return buildString {
            parcelPhoto.exif?.make
            parcelPhoto.exif?.model
        }
    }

    private fun sharePhoto() {
        val shareUrl = parcelPhoto.links?.html
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareUrl)
        startActivity(Intent.createChooser(shareIntent, "Share URL"))
    }

}


