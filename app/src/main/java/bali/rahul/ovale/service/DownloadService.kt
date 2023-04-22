package bali.rahul.ovale.service

import android.app.DownloadManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.os.IBinder
import android.widget.Toast
import bali.rahul.ovale.dataModel.Photo
import bali.rahul.ovale.utils.Internet

class DownloadService : Service() {

    private val downloadManager: DownloadManager by lazy {
        getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    fun downloadPhoto(parcelPhoto: Photo) {


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

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}
