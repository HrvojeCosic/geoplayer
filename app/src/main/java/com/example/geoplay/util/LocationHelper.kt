package com.example.geoplay.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Criteria
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.IOException
import java.util.Locale

class LocationHelper(private val context: Context) {
    private var locationManager: LocationManager? = null
    private var geocoder: Geocoder? = null
    var cityName: String = ""

    init {
        geocoder = Geocoder(context, Locale.getDefault())
    }

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) { }
    }

    fun startLocationUpdates() {
        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                100
            );
        } else {
            val criteria = Criteria().apply {
                accuracy = Criteria.ACCURACY_FINE
                powerRequirement = Criteria.POWER_LOW
            }
            val provider = locationManager?.getBestProvider(criteria, true)
            if (provider != null) {
                locationManager?.requestLocationUpdates(provider, 60000, 1000f, locationListener)
                updateCityName()
            }
        }
    }

    fun stopLocationUpdates() {
        locationManager?.removeUpdates(locationListener)
    }

    fun updateCityName() {
        try {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            if (
                context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
            ) {
                val lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

                if (lastKnownLocation != null) {
                    val latitude = lastKnownLocation.latitude
                    val longitude = lastKnownLocation.longitude

                    val addresses: List<Address> = geocoder?.getFromLocation(latitude, longitude, 1) ?: emptyList()
                    if (addresses.isNotEmpty()) {
                        cityName = addresses[0].locality ?: ""
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

