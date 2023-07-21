package bali.rahul.ovale.service

import android.app.DownloadManager
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import bali.rahul.ovale.dataModel.Photo
import bali.rahul.ovale.utils.Internet

class DownloadService(private val context: Context) {

    private lateinit var downloadManager: DownloadManager

    fun downloadPhoto(parcelPhoto: Photo) {
        downloadManager = context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager

        val request = DownloadManager.Request(Uri.parse(parcelPhoto.urls?.full)).apply {
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
            setTitle("Downloading Photo from ${parcelPhoto.user?.name}.jpg")
            setDescription("Downloading an image from ${parcelPhoto.links?.html}")
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setDestinationInExternalPublicDir(
                Environment.DIRECTORY_PICTURES,
                "Ovale/${parcelPhoto.user?.username}-${parcelPhoto.id}.jpg"
            )
        }
        downloadManager.enqueue(request)
        if (Internet.isNetworkAvailable(context)) {
            Toast.makeText(context, "Downloading started...", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "No Internet, Downloading paused...", Toast.LENGTH_SHORT).show()
        }
    }

}
