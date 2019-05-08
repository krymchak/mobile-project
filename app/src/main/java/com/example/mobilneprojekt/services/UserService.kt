package com.example.mobilneprojekt.services

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST("api/login")
    fun login(@Body user: UserCredentialsDTO): Call<String>
    @POST("api/register")
    fun register(@Body user: UserDTO): Call<Void>
}

data class UserCredentialsDTO(val username: String, val password: String)
data class UserDTO(val name: String, val surname: String, val dateOfBirth: String, val pesel: String, val login: String, val password: String)