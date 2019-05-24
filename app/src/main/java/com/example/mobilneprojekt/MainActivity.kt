package com.example.mobilneprojekt

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.mobilneprojekt.services.ServiceBuilder
import com.example.mobilneprojekt.services.UserCredentialsDTO
import com.example.mobilneprojekt.services.UserDTO
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.user_registration.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private lateinit var preferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        preferences = getSharedPreferences("com.herokuapp.mobilne-projekt", Context.MODE_PRIVATE)

        usernameLog.visibility = View.VISIBLE
        passLog.visibility = View.VISIBLE
        buttonLog.visibility = View.VISIBLE
        regLog.visibility = View.VISIBLE

        regLog.setOnClickListener {
            showRegisterLayout()
        }

        registerUserBt.setOnClickListener {
            val user = UserDTO(
                name.text.toString(),
                surname.text.toString(),
                dateOfBirth.text.toString(),
                pesel.text.toString(),
                username.text.toString(),
                password.text.toString()
            )
            val call = ServiceBuilder.getUserService().register(user)
            val context : Context = this
            call.enqueue(object : Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("OCL", "Fail to register User!")
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    Log.d("OCL", "Success")
                    if (response.isSuccessful) {
                        showLoginLayout()
                        with(preferences.edit()) {
                            putString("token", response.body())
                            apply()
                        }
                        reroute()
                    }
                    else {
                        Toast.makeText(context, "Fail to register User!", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }

        buttonLog.setOnClickListener {
            val user = UserCredentialsDTO(usernameLog.text.toString(), passLog.text.toString())
            val call = ServiceBuilder.getUserService().login(user)
            call.enqueue(object : Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("Onfail", "Failed to login")
                    fail()
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    Log.d("onSucc", "Success")
                    if (response.body() != null) {
                        with(preferences.edit()) {
                            putString("token", response.body())
                            apply()
                        }
                        reroute()
                    } else {
                        Log.e("onSucc", "error")
                        fail()
                    }
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        showLoginLayout()
        preferences = getSharedPreferences("com.herokuapp.mobilne-projekt", Context.MODE_PRIVATE)
        if (preferences.getString("token", "") != "") {
            reroute()
            return
        }
    }

    override fun onBackPressed() {
        // if registration is visible then go to login else close app
        if (registration_layout.visibility == View.VISIBLE) {
            showLoginLayout()
        } else {
            super.onBackPressed()
        }
    }

    private fun showRegisterLayout() {
        registration_layout.visibility = View.VISIBLE
        usernameLog.visibility = View.GONE
        passLog.visibility = View.GONE
        buttonLog.visibility = View.GONE
        regLog.visibility = View.GONE
    }

    private fun showLoginLayout() {
        registration_layout.visibility = View.GONE
        usernameLog.visibility = View.VISIBLE
        passLog.visibility = View.VISIBLE
        buttonLog.visibility = View.VISIBLE
        regLog.visibility = View.VISIBLE

        usernameLog.text.clear()
        passLog.text.clear()
    }

    fun fail() {
        Toast.makeText(this, "Either Username or Password are invalid!", Toast.LENGTH_SHORT).show()
    }

    fun reroute() {
        Toast.makeText(this, "Logged In", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, ListOfCarsActivity::class.java)
        startActivity(intent)
    }
}