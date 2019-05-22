package com.example.mobilneprojekt

import android.content.Context
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.mobilneprojekt.services.CarIdDTO
import com.example.mobilneprojekt.services.ServiceBuilder
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailInfoCarActivity : FragmentActivity() {

    var id = 0
    private var token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_info_car)

        id = intent.getIntExtra("id",0)
        findViewById<TextView>(R.id.name).text = intent.getStringExtra("name")
        findViewById<TextView>(R.id.category).text = intent.getStringExtra("category")
        findViewById<TextView>(R.id.year).text = intent.getStringExtra("year")
        findViewById<TextView>(R.id.seats).text = intent.getStringExtra("seats")
        findViewById<TextView>(R.id.dmc).text = intent.getStringExtra("dmc")
        findViewById<TextView>(R.id.mileage).text = intent.getStringExtra("mileage")
        findViewById<TextView>(R.id.price).text = getString(R.string.priceFormatString, intent.getStringExtra("price"))

        val image = findViewById<ImageView>(R.id.imageView4)
        val url = "${ServiceBuilder.getUrl()}${intent.getStringExtra("image")}"
        Picasso.get().load(url).centerCrop().fit().into(image)

        val preferences = getSharedPreferences("com.herokuapp.mobilne-projekt", Context.MODE_PRIVATE)
        token = preferences.getString("token", "") ?: ""
        if (token == "") {
            finish()
        }
    }

    fun rent(view: View)
    {
        Log.v("click", "Button ${view.id} pressed")
        val car = CarIdDTO(id)
        val context = this
        ServiceBuilder.getRentalService().rentCar(token, car).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Log.v("info", "post submitted to API")
                if (response.isSuccessful) {
                    Toast.makeText(context,"Samochód został wynajęty)", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(context,"Nie możesz wynająć samochodu (masz już wynajęty samochód lub ktoś cie uprzedził)", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("error", "Unable to submit post to API")
            }
        }) }
}