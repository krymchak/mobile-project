package com.example.mobilneprojekt

import com.google.android.gms.maps.model.LatLng

data class HistoryItem (
    var id: Int,
    var info: CarsInfo,
    var date: String,
    var startTime: String,
    var endTime: String,
    var vehicleImage: Int,
    var fromCoords: LatLng,
    var toCoords: LatLng,
    var totalTime: String
)