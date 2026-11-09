package com.example.canalizator

object DataHolder {
    var speed: Double = 0.0
    var rpm: Double = 0.0
    var angle: Double = -201.0
    var lat: Double = -201.0
    var lon: Double = -201.0

    fun reset() {
        speed = 0.0
        rpm = 0.0
        angle = -201.0
        lat = -201.0
        lon = -201.0
    }
}