package com.indexdev.tourin.ui

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.gms.maps.model.LatLng
import kotlin.math.acos
import kotlin.math.sin

var locationList: MutableList<Location> = ArrayList()
var latLngTour : LatLng? =null
var distanceLocation : Double? = null
var inArea = false
fun setFullScreen(window: Window) {
    WindowCompat.setDecorFitsSystemWindows(window, false)
}

fun lightStatusBar(window: Window, isLight: Boolean = true) {
    val wic = WindowInsetsControllerCompat(window, window.decorView)
    wic.isAppearanceLightStatusBars = isLight
    wic.isAppearanceLightNavigationBars = isLight
}
fun alertDialog(context:Context, title:String, message:String ){
    AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(message)
        .setCancelable(false)
        .setPositiveButton("OK") { positive, _ ->
            positive.dismiss()
        }
        .show()
}
fun alertDialog(context:Context, title:String, message:String,action:Boolean ){
    AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(message)
        .setCancelable(false)
        .setPositiveButton("OK") { positive, _ ->
            positive.dismiss()
            action
        }
        .show()
}
fun alertDialog(context:Context, title:String, message:String,navigate:Unit){
    AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(message)
        .setCancelable(false)
        .setPositiveButton("OK") { positive, _ ->
            positive.dismiss()
            navigate
        }
        .show()
}

fun getBitmapFromVectorDrawable(context: Context, drawableId: Int): Bitmap {
    val drawable = ContextCompat.getDrawable(context, drawableId)
    val bitmap = Bitmap.createBitmap(
        drawable!!.intrinsicWidth,
        drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}

// calculate distance between 2 point
fun calculateDistanceInKM(
    lat1: Double,
    lon1: Double,
    lat2: Double,
    lon2: Double
): Double {
    val theta = lon1 - lon2
    var dist =
        sin(deg2rad(lat1)) * sin(deg2rad(lat2)) +
                kotlin.math.cos(deg2rad(lat1)) * kotlin.math.cos(deg2rad(lat2)) * kotlin.math.cos(
            deg2rad(theta)
        )
    dist = acos(dist)
    dist = rad2deg(dist)
    dist *= 60 * 1.1515
    dist *= 1.609344
    return dist

}

private fun deg2rad(deg: Double): Double {
    return deg * Math.PI / 180.0
}

private fun rad2deg(rad: Double): Double {
    return rad * 180.0 / Math.PI
}
