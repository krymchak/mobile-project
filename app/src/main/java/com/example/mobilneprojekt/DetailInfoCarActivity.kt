package com.example.mobilneprojekt

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.mobilneprojekt.services.CarIdDTO
import com.example.mobilneprojekt.services.ServiceBuilder
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailInfoCarActivity : FragmentActivity() {

    var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_info_car)

        id = intent.getIntExtra("id",0)
        findViewById<TextView>(R.id.name).text=intent.getStringExtra("name")
        findViewById<TextView>(R.id.category).text=intent.getStringExtra("category")
        findViewById<TextView>(R.id.year).text=intent.getStringExtra("year")
        findViewById<TextView>(R.id.seats).text=intent.getStringExtra("seats")
        findViewById<TextView>(R.id.dmc).text=intent.getStringExtra("dmc")
        findViewById<TextView>(R.id.mileage).text=intent.getStringExtra("mileage")
        findViewById<TextView>(R.id.price).text=intent.getStringExtra("price")+ "$"

        var image = findViewById(R.id.imageView4) as ImageView
        val url = "${ServiceBuilder.getUrl()}${intent.getStringExtra("image")}"
        Picasso.get().load(url).centerCrop().fit().into(image)
    }

    fun rent(v: View)
    {
        val car = CarIdDTO(id)
        ServiceBuilder.getRentalService().rentCar("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJsb2dpbiI6ImFkbWluIiwiaWF0IjoxNTU3NzQ2MjU0LCJleHAiOjE1NzA3MDYyNTR9.oaSsRbNO4vio9xkvEG70L-DcJ6LsDPaRyM_hxh3uAfU", car).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {

                if (response.isSuccessful()) {
                    Log.v("info", "post submitted to API")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("error", "Unable to submit post to API")
            }
        }) }
}