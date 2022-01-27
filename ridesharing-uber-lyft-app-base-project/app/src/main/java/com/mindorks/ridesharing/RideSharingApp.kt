package com.mindorks.ridesharing

import android.app.Application
import com.google.android.libraries.places.api.Places
import com.google.maps.GeoApiContext
import com.mindorks.ridesharing.simulator.Simulator

class RideSharingApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Places.initialize(applicationContext, "AIzaSyCR8J44f3bVZZvKR5CXlEkRF-okijQXo28");
        Simulator.geoApiContext = GeoApiContext.Builder()
            .apiKey("AIzaSyCR8J44f3bVZZvKR5CXlEkRF-okijQXo28")
            .build()
    }

}