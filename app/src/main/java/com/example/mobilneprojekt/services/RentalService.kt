package com.example.mobilneprojekt.services

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface RentalService {
    @GET("sec/cars")
    fun getCars(@Header("Authorization") token: String): Call<List<CarDTO>>

    @GET("sec/history")
    fun getHistory(@Header("Authorization") token: String): Call<List<HistoryEntryDTO>>

    @POST("sec/rent")
    fun rentCar(@Header("Authorization") token: String, @Body carId: CarIdDTO): Call<Void>

    @POST("sec/return")
    fun returnCar(@Header("Authorization") token: String, @Body carId: CarIdDTO): Call<Void>

    @POST("sec/add")
    fun addCar(@Header("Authorization") token: String, @Body car: NewCarDTO): Call<Void>

    @GET("sec/rental")
    fun getRental(@Header("Authorization") token: String): Call<CarDTO?>

    @GET("sec/myCars")
    fun getMyCars(@Header("Authorization") token: String): Call<List<CarExtDTO>>

}

data class CarDTO(val id: Int, val name: String, val year: String, val dmc: Int, val seats: Int, val mileage: Int, val category: String, val image: String, val owner: String, val price: Float, val security: Float, val latitude: Double, val longitude: Double)
data class CarExtDTO(val id: Int, val name: String, val year: String, val dmc: Int, val seats: Int, val mileage: Int, val category: String, val image: String, val owner: String, val price: Float, val security: Float, val latitude: Double, val longitude: Double, val isRented: Boolean)
data class HistoryEntryDTO(val date: String, val id: Int, val name: String, val year: String, val dmc: Int, val seats: Int, val mileage: Int, val category: String, val image: String, val owner: String, val price: Float, val security: Float, val latitude: Double, val longitude: Double)
data class CarIdDTO(val carId: Int)
data class NewCarDTO(val name: String, val year: Int, val dmc: Int, val seats: Int, val mileage: Int, val image: String, val price: Float, val security: Float, val latitude: Double, val longitude: Double)