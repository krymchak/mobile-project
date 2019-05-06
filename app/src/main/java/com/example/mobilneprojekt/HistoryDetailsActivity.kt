package com.example.mobilneprojekt

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_history_details.*


class HistoryDetailsActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_details)

        if (intent != null) {

            val id = intent.getStringExtra("id")
            //get item from DB by ID.toInt()

            val c = LatLng(51.109687, 17.058089)
            val item = (HistoryItem(0, CarsInfo("CarName", "C", 12F), "12.12.12", "15:30",
                "15:42", R.drawable.ic_launcher_background,  c, c,  "12H:12M:12S"))

            showItem(item)
        }

    }


    fun showItem(item: HistoryItem) {
        imageView.setImageResource(item.vehicleImage)
        vname.text = item.info.getName()

    }


}