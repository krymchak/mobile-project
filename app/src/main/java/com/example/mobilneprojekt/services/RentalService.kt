package com.example.mobilneprojekt.services

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RentalService {
    @GET("sec/cars")
    fun getCars(): Call<List<CarDTO>>

    @GET("sec/history")
    fun getHistory(): Call<List<HistoryEntryDTO>>
}

data class CarDTO(val id: Int, val name: String, val year: String, val dmc: Int, val seats: Int, val mileage: Int, val category: String, val image: String)
data class HistoryEntryDTO(val date: String, val id: Int, val name: String, val year: String, val dmc: Int, val seats: Int, val mileage: Int, val category: String, val image: String)