package com.example.mobilneprojekt

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.util.Base64.DEFAULT
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import com.example.mobilneprojekt.services.CarDTO
import com.example.mobilneprojekt.services.NewCarDTO
import com.example.mobilneprojekt.services.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class AddCarActivity : AppCompatActivity() {

    lateinit var imageBitmap : Bitmap
    lateinit var token : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_car)

        imageBitmap = intent.extras.get("data") as Bitmap
        token = intent.getStringExtra("token")
        findViewById<ImageView>(R.id.carImage).apply {
            setImageBitmap(imageBitmap)
        }
    }

    fun onAddCar(view: View) {
        val name = findViewById<EditText>(R.id.editText).text.toString()
        val year = findViewById<EditText>(R.id.editText2).text.toString().toInt()
        val dmc = findViewById<EditText>(R.id.editText3).text.toString().toInt()
        val seats = findViewById<EditText>(R.id.editText4).text.toString().toInt()
        val mileage = findViewById<EditText>(R.id.editText5).text.toString().toInt()
        val price =  findViewById<EditText>(R.id.editText6).text.toString().toFloat()
        val sec = findViewById<EditText>(R.id.editText7).text.toString().toFloat()

        val stream = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val b64Image = Base64.encodeToString(stream.toByteArray(), DEFAULT)
//        val sb = StringBuilder()
//        sb.append("0x")
//        for (byte in stream.toByteArray()) {
//            sb.append(String.format("%02X", byte))
//        }


        val car = NewCarDTO(name, year, dmc, seats, mileage, b64Image, price, sec)
        // TODO: Add dialog to confirm

        val call = ServiceBuilder.getRentalService().addCar(token, car)
        Log.d("AC", "Enqueue")

        call.enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("AC", "Failed to add car")
                setResult(Activity.RESULT_OK, Intent().apply { putExtra("data", false) })
                finish()
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                setResult(Activity.RESULT_OK, Intent().apply { putExtra("data", true) })
                finish()
            }
        })
    }
}