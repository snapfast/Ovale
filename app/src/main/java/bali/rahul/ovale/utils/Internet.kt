package bali.rahul.ovale.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build

class Internet {

    companion object {
        fun isNetworkAvailable(context: Context): Boolean {

            lateinit var network: Network

            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                network = connectivityManager.activeNetwork ?: return false
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val networkCapabilities =
                    connectivityManager.getNetworkCapabilities(network) ?: return false


                return when {
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    // for other devices which are able to connect with Ethernet
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    // for VPN connections
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> true
                    else -> false
                }
            } else {
                return connectivityManager.activeNetworkInfo!!.isConnected
            }
        }
    }
}