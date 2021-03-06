package com.mindorks.ridesharing.ui.maps

import android.util.JsonReader
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.gson.JsonObject
import com.mindorks.ridesharing.data.network.NetworkService
import com.mindorks.ridesharing.simulator.WebSocket
import com.mindorks.ridesharing.simulator.WebSocketListener
import com.mindorks.ridesharing.utils.Constants
import org.json.JSONObject

class MapsPresenter(private val networkService: NetworkService) : WebSocketListener {

    companion object {
        private const val TAG = "MapsPresenter"
    }

    private var view: MapsView? = null
    private lateinit var webSocket: WebSocket

    fun onAttach(view: MapsView) {
        this.view = view
        webSocket = networkService.createWebSocket(this)
        webSocket.connect()
    }

    fun onDetach() {
        webSocket.disconnect()
        view = null
    }

    fun requestNearbyCabs(latLng: LatLng) {
        val jsonObject = JSONObject()
        jsonObject.put(Constants.TYPE, Constants.NEAR_BY_CABS)
        jsonObject.put(Constants.LAT, latLng.latitude)
        jsonObject.put(Constants.LNG, latLng.longitude)
        webSocket.sendMessage(jsonObject.toString())
    }

    private fun handleOnMessageNearByCabs(jsonObject: JSONObject) {
        val nearbyCabLocation = arrayListOf<LatLng>()
        val jsonArray = jsonObject.getJSONArray(Constants.LOCATIONS)
        for (i in 0 until jsonArray.length()) {
            val lat = (jsonArray.get(i) as JSONObject).getDouble(Constants.LAT)
            val lng = (jsonArray.get(i) as JSONObject).getDouble(Constants.LNG)
            val latLng = LatLng(lat, lng)
            nearbyCabLocation.add(latLng)
        }
        view?.showNearByCabs(nearbyCabLocation)
    }

    override fun onConnect() {
        Log.d(TAG, "onCreate")
    }

    override fun onMessage(data: String) {
        Log.d(TAG, "onMessage data:$data")
        val jsonobject = JSONObject(data)
        when (jsonobject.getString(Constants.TYPE)) {
            Constants.NEAR_BY_CABS -> {
                handleOnMessageNearByCabs(jsonobject)
            }
        }
    }

    override fun onDisconnect() {
        Log.d(TAG, "onDisconnect")
    }

    override fun onError(error: String) {
        Log.d(TAG, "onError:$error")
    }
}