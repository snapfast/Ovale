package bali.rahul.ovale

import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import bali.rahul.ovale.dataModel.Photo
import bali.rahul.ovale.databinding.ActivityPhotoBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import java.io.File

class PhotoActivity : AppCompatActivity() {


    private lateinit var parcelPhoto: Photo

    val TAG = "<<<<<<<<<<<<PhotoActivity>>>>>>>>>>>>"

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)


        val photoImage: ImageView = findViewById(R.id.imageView)

        // Set the toolbar with Back button
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        // Retrieve the parcelable data from the intent
        parcelPhoto = intent.getParcelableExtra<Photo>("photo", Photo::class.java)!!

        Log.i(TAG, "Received photo data: $parcelPhoto")

        // Set the image using Glide

        Log.d(TAG, "Received photo data: $parcelPhoto")

        val requestOptions = RequestOptions().placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)

        Glide.with(this).applyDefaultRequestOptions(requestOptions).load(parcelPhoto.urls?.regular)
            .transition(DrawableTransitionOptions.withCrossFade())
            .diskCacheStrategy(com.bumptech.glide.load.engine.DiskCacheStrategy.ALL)
            .into(photoImage)
        photoImage.contentDescription = parcelPhoto.altDescription

        // Set the text
        binding.descriptionTextView.text = parcelPhoto.altDescription
        binding.dateTextView.text = parcelPhoto.createdAt
        binding.linkTextView.text = parcelPhoto.links?.html
        binding.toolbar.title = parcelPhoto.user?.name + " - " + parcelPhoto.user?.username

        // On Click Download Button, download the image using Glide to new_folder
        binding.downloadPhoto.setOnClickListener {

            // Register the permissions callback, which handles the user's response to the
            // system permissions dialog. Save the return value, an instance of
            // ActivityResultLauncher. You can use either a val, as shown in this snippet,
            // or a lateinit var in your onAttach() or onCreate() method.
            val requestPermissionLauncher = registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.

                } else {
                    // Explain to the user that the feature is unavailable because the
                    // feature requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                    val parentLayout = findViewById<LinearLayout>(R.id.linearLayout)

                    val textView = TextView(this)
                    textView.text = "Hello World!"
                    textView.setTextColor(resources.getColor(R.color.red, null))

                    parentLayout.addView(textView)
                }
            }
            when {
                ContextCompat.checkSelfPermission(
                    baseContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    downloadPhoto()
                }
                shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
                    Toast.makeText(this, "Permission Required", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    requestPermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }
            Toast.makeText(this, "Image Downloaded", Toast.LENGTH_SHORT).show()
        }

        binding.setWallpaper.setOnClickListener {
            Glide.with(this).asBitmap().load(parcelPhoto.urls?.full)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap, transition: Transition<in Bitmap>?
                    ) {
                        val wallpaperManager = WallpaperManager.getInstance(applicationContext)
                        wallpaperManager.setBitmap(resource)
                        Toast.makeText(
                            this@PhotoActivity, "Wallpaper Set Successfully", Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        // do nothing
                    }
                })
        }
    }

    private fun downloadPhoto() {

        Glide.with(this).downloadOnly().load(parcelPhoto.urls?.full)
            .addListener(object : RequestListener<File> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<File>?,
                    isFirstResource: Boolean
                ): Boolean {
                    // handle failure
                    Toast.makeText(
                        this@PhotoActivity, "Image Download Failed...", Toast.LENGTH_SHORT
                    ).show()
                    return false
                }

                override fun onResourceReady(
                    resource: File?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<File>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    // handle success
                    // get the downloaded file and move it to your app's dedicated folder
                    val storageDir = File(baseContext.getExternalFilesDir(null), "Ovale")
                    if (!storageDir.exists()) {
                        storageDir.mkdirs()
                    }
                    val targetFile = File(storageDir, "${parcelPhoto.id}.jpg")
                    Log.d(TAG, "onResourceReady: ${targetFile.absolutePath}")
                    resource?.absoluteFile?.copyTo(targetFile, true)
                    return true
                }
            }).submit()
    }


    fun setAsWallpaper() {
        Glide.with(this).asBitmap().load(parcelPhoto.urls?.full)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap, transition: Transition<in Bitmap>?
                ) {
                    val wallpaperManager = WallpaperManager.getInstance(this@PhotoActivity)
                    wallpaperManager.setBitmap(resource)
                    Toast.makeText(this@PhotoActivity, "Wallpaper Set", Toast.LENGTH_SHORT).show()
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // do nothing
                }
            })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            100 -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                    downloadPhoto()
                } else {
                    // Explain to the user that the feature is unavailable because
                    // the feature requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }


}


