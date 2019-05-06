package com.example.mobilneprojekt


import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng


class HistoryListActivity : AppCompatActivity(){

    var historyItems: ArrayList<HistoryItem>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_list)

        historyItems = ArrayList<HistoryItem>() //get from DB

        val c = LatLng(51.109687, 17.058089)

        historyItems!!.add(HistoryItem(0, CarsInfo("CarName", "C", 12F), "12.12.12", "15:30",
            "15:42", R.drawable.ic_launcher_background,  c, c,  "12H:12M:12S"))
        historyItems!!.add(HistoryItem(0, CarsInfo("CarName", "C", 12F), "12.12.12", "15:30",
            "15:42", R.drawable.ic_launcher_background,  c, c,  "12H:12M:12S"))
        historyItems!!.add(HistoryItem(0, CarsInfo("CarName", "C", 12F), "12.12.12", "15:30",
            "15:42", R.drawable.ic_launcher_background,  c, c,  "12H:12M:12S"))


        val recyclerView = findViewById<RecyclerView>(R.id.historyView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = HistoryAdapter(historyItems!!, clickListener = {appClickListener(it)})

    }

    fun appClickListener(position: Int) {
        val intent = Intent(this, HistoryDetailsActivity::class.java)
        val id = historyItems!![position].id.toString()
        intent.putExtra("id", id)
        startActivity(intent)
    }


}
