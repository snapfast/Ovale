package bali.rahul.ovale.service

import android.app.WallpaperManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.Toast
import bali.rahul.ovale.storage.OvaleDatabase
import bali.rahul.ovale.storage.Storage
import bali.rahul.ovale.storage.model.PhotoStore
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class OvaleWallpaperService : BroadcastReceiver() {

    private var photoStoreList: List<PhotoStore> = listOf()

    // Define a coroutine scope
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    // Create a suspend function that calls the DAO's read function inside a coroutine
    private suspend fun readFavourites(context: Context) {
        withContext(Dispatchers.IO) {
            val db = OvaleDatabase.getInstance(context)
            val photoDao = db.photoDao() // initialized photoDao
            photoStoreList = photoDao.getAllPhotos()
        }
    }

    // This is the receiver that will be called when the alarm is triggered
    override fun onReceive(context: Context?, intent: Intent?) {

        // Read all photoStore from DB
        if (context == null) {
            Log.d("OvaleWallpaperService", "onReceive: context is null")
            return
        }

        // write data from db to textview
        coroutineScope.launch {
            readFavourites(context)

            val storage = Storage(context)

            // read value from shared prefs
            var wallpaperCounter: Int? = storage.get("wallpaperNumber", 1) as Int?
            if (wallpaperCounter == null) {
                Log.d("OvaleWallpaperService", "onReceive: wallpaperCounter is null")
                storage.save("wallpaperNumber", 0)
                wallpaperCounter = 0
            }
            // check photoStoreList is empty
            if (photoStoreList.isEmpty()) {
                Log.d("OvaleWallpaperService", "onReceive: photoStoreList is empty")
                return@launch
            }
            // check if wallpaperCounter is greater than photoStoreList size
            if (wallpaperCounter >= photoStoreList.size) {
                Log.d(
                    "OvaleWallpaperService",
                    "onReceive: wallpaperCounter is greater than photoStoreList size"
                )
                wallpaperCounter = 0
            }
            Log.d("OvaleWallpaperService", "onReceive: $wallpaperCounter")
            Log.d("OvaleWallpaperService", "onReceive: ${photoStoreList[wallpaperCounter]}")

            // set wallpaper
            setAsWallpaper(photoStoreList[wallpaperCounter].url_regular!!, context)
            Log.d("OvaleWPService", "wallpaper applied: ${photoStoreList[wallpaperCounter]}")
            storage.save("wallpaperNumber", wallpaperCounter + 1)
        }
    }

    companion object {

        val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)

        fun setAsWallpaper(url: String, context: Context) {
            Glide.with(context).asBitmap().load(url).into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap, transition: Transition<in Bitmap>?
                ) {
                    val wallpaperManager = WallpaperManager.getInstance(context)
                    coroutineScope.launch {
                        withContext(Dispatchers.Main) {
                            // Update the UI here
                            Toast.makeText(context, "Wallpaper Set", Toast.LENGTH_SHORT).show()
                        }
                        wallpaperManager.setBitmap(resource)
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // do nothing
                }
            })
        }
    }

}
