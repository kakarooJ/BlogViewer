package com.kakaroo.blogviewer.utility

import android.content.Context
import android.net.ConnectivityManager

class MyUtility {

    object MyUtility {}

    fun isNetworkConnected(context: Context): Boolean {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        val wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        val wimax = manager.getNetworkInfo(ConnectivityManager.TYPE_WIMAX)
        var bwimax = false
        if (wimax != null) {
            bwimax = wimax.isConnected
        }
        if (mobile != null) {
            if (mobile.isConnected || wifi!!.isConnected || bwimax) {
                return true
            }
        } else {
            if (wifi!!.isConnected || bwimax) {
                return true
            }
        }
        return false
    }
}