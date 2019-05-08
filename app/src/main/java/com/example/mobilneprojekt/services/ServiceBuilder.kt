package com.example.mobilneprojekt.services

import retrofit2.Retrofit
import java.lang.Exception

object ServiceBuilder {
    private val userService: UserService
    private val rentalService: RentalService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://mobilne-projekt.herokuapp.com/")
            .build()
        userService = retrofit.create(UserService::class.java)
        rentalService = retrofit.create(RentalService::class.java)
    }

    fun getUserService(): UserService {
        return userService
    }
    fun getRentalService(): RentalService {
        return rentalService
    }
}