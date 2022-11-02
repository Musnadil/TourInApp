package com.indexdev.tourin.services

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.create
import com.indexdev.tourin.ui.*

class LocationService : Service() {
    companion object{
        const val DISTANCE = "distance"
        const val UPDATE_DISTANCE = "update_distance"

    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val locationRequest: LocationRequest = create().apply {
        interval = 3000
        fastestInterval = 3000
        priority = Priority.PRIORITY_BALANCED_POWER_ACCURACY
        maxWaitTime = 5000
    }

    private var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationList = locationResult.locations
            if (!locationList.isNullOrEmpty()) {
                val location = locationList.last()

                distanceLocation = latLngTour?.let {
                    calculateDistanceInKM(
                        location.latitude, location.longitude,
                        it.latitude, it.longitude
                    )
                }
                val intent = Intent(DISTANCE)
                intent.putExtra(UPDATE_DISTANCE, distanceLocation)
                sendBroadcast(intent)

//                if (distance != null) {
//                    if (distance!! <= 2) {
//                        if (distance!! >= 3) {
//                        onDestroy()
//                            //notifikasi untuk menampilkan user harus kasih rating, kemudian ketika di klik open dialog rating
//                        }
//                    }
//                }
            }
        }
    }


    /*******************/

    @SuppressLint("MissingPermission")
    override fun onCreate() {
        super.onCreate()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createNotificationChanel() else startForeground(
            1,
            Notification()
        )

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {

            Toast.makeText(applicationContext, "Permission required", Toast.LENGTH_LONG).show()
            return
        } else {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChanel() {
        val notificationChannelId = "Location channel id"
        val channelName = "Background Service"
        val chan = NotificationChannel(
            notificationChannelId,
            channelName,
            NotificationManager.IMPORTANCE_NONE
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val manager = (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
        manager.createNotificationChannel(chan)
        val notificationBuilder =
            NotificationCompat.Builder(this, notificationChannelId)
        val notification: Notification = notificationBuilder.setOngoing(true)
            .setContentTitle("Location updates:")
            .setPriority(NotificationManager.IMPORTANCE_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()

        startForeground(2, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}