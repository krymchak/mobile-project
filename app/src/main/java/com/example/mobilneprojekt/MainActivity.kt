package com.example.mobilneprojekt

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this, ListOfCars::class.java)
        startActivity(intent)
    }
}

// Rafal Marcin Mlodzieniak
//MM


    //Z mojej strony to tyle - Andrzej


//VK