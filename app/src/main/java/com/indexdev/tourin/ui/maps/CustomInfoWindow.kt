package com.indexdev.tourin.ui.maps

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.indexdev.tourin.R
import com.indexdev.tourin.data.model.response.ResponsePOI

class CustomInfoWindow(private val context: Context) : GoogleMap.InfoWindowAdapter {
    override fun getInfoContents(p0: Marker): View? {
        return null
    }

    override fun getInfoWindow(marker: Marker): View? {
        val view = (context as AppCompatActivity)
            .layoutInflater
            .inflate(R.layout.layout_tooltip_marker, null)

        val tvPlaceName = view.findViewById<TextView>(R.id.tv_place_name)
        val root = view.findViewById<ConstraintLayout>(R.id.root).rootView
        val infoWindowData = marker.tag as ResponsePOI?
        return if (infoWindowData != null) {
            tvPlaceName.text = infoWindowData.namaFasilitas
            view
        }else{
            null
        }
    }
}