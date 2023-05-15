package com.indexdev.tourin.ui.maps

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.indexdev.tourin.R
import com.indexdev.tourin.data.model.response.ResponsePOI
import com.indexdev.tourin.data.model.response.ResponseUserMitra

class CustomInfoWindow(private val context: Context) : GoogleMap.InfoWindowAdapter {
    override fun getInfoContents(p0: Marker): View? {
        return null
    }

    override fun getInfoWindow(marker: Marker): View? {
        val view = (context as AppCompatActivity)
            .layoutInflater
            .inflate(R.layout.layout_tooltip_marker, null)
        val tvPlaceName = view.findViewById<TextView>(R.id.tv_place_name)

        return when (marker.tag) {
            is ResponsePOI? -> {
                val infoWindowData = marker.tag as ResponsePOI?
                tvPlaceName.text = infoWindowData?.namaFasilitas
                view
            }
            is ResponseUserMitra? -> {
                val infoWindowData = marker.tag as ResponseUserMitra?
                tvPlaceName.text = infoWindowData?.namaUsaha
                view
            }
            "i" -> {
                null
            }
            else -> {
                null
            }
        }
    }
}