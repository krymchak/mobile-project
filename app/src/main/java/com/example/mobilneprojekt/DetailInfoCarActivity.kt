package com.example.mobilneprojekt

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.widget.TextView

class DetailInfoCarActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_info_car)

        findViewById<TextView>(R.id.name).text=intent.getStringExtra("name")
        findViewById<TextView>(R.id.category).text=intent.getStringExtra("category")
        findViewById<TextView>(R.id.year).text=intent.getStringExtra("year")
        findViewById<TextView>(R.id.seats).text=intent.getStringExtra("seats")
        findViewById<TextView>(R.id.dmc).text=intent.getStringExtra("dmc")
        findViewById<TextView>(R.id.mileage).text=intent.getStringExtra("mileage")
    }
}