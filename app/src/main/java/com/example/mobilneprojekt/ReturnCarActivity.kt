package com.example.mobilneprojekt

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.example.mobilneprojekt.services.CarDTO
import com.example.mobilneprojekt.services.CarIdDTO
import com.example.mobilneprojekt.services.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReturnCarActivity : AppCompatActivity() {

    var id = 0
    var token = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val preferences = getSharedPreferences("com.herokuapp.mobilne-projekt", Context.MODE_PRIVATE)
        token = preferences.getString("token", "") ?: ""
        if (token == "") {
            finish()
            return
        }
        val call = ServiceBuilder.getRentalService().getRental(token)
        call.enqueue(object : Callback<CarDTO?> {
            override fun onFailure(call: Call<CarDTO?>, t: Throwable) {
                Log.e("rest", "Failed to get rental")
            }

            override fun onResponse(call: Call<CarDTO?>, response: Response<CarDTO?>) {
                val body = response.body()
                if (body != null) {
                    id = body.id
                    setContentView(R.layout.detail_info_car)
                    findViewById<TextView>(R.id.name).text = body.name
                    findViewById<TextView>(R.id.category).text = body.category
                    findViewById<TextView>(R.id.year).text = body.year
                    findViewById<TextView>(R.id.seats).text = body.seats.toString()
                    findViewById<TextView>(R.id.dmc).text = body.dmc.toString()
                    findViewById<TextView>(R.id.mileage).text = body.mileage.toString()
                    findViewById<TextView>(R.id.price).text = getString(R.string.priceFormat, body.price)
                    findViewById<TextView>(R.id.button2).text = getString(R.string.ret)
                    findViewById<Button>(R.id.button2).setOnClickListener {
                        val callReturn = ServiceBuilder.getRentalService().returnCar(token, CarIdDTO(id))
                        callReturn.enqueue(object : Callback<Void> {
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                finish()
                            }
                            override fun onFailure(call: Call<Void>, t: Throwable) {
                                Log.e("rest", "Failed to return car")
                            }
                        })
                    }
                } else {
                    setContentView(R.layout.error_layout)
                    findViewById<TextView>(R.id.textView).apply {
                        text = getString(R.string.noCars)
                    }
                }
            }
        })
    }
}
