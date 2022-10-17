package com.indexdev.tourin.ui

import android.app.AlertDialog
import android.content.Context
import android.view.Window
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat

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