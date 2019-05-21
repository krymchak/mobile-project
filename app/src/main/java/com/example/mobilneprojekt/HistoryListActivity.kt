package com.example.mobilneprojekt

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.example.mobilneprojekt.services.HistoryEntryDTO
import com.example.mobilneprojekt.services.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HistoryListActivity : AppCompatActivity(), HistoryAdapter.ClickListener {

    var listOfHistory = ArrayList<HistoryEntryDTO>()
    lateinit var adapter: HistoryAdapter
    private var token = ""

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_of_cars)

        val preferences = getSharedPreferences("com.herokuapp.mobilne-projekt", Context.MODE_PRIVATE)
        token = preferences.getString("token", "") ?: ""
        if (token == "") {
            finish()
            return
        }

        val callCars = ServiceBuilder.getRentalService().getHistory(token)
        callCars.enqueue(object : Callback<List<HistoryEntryDTO>> {
            override fun onFailure(call: Call<List<HistoryEntryDTO>>, t: Throwable) {
                Log.e("call", "Failed to get history")
            }

            override fun onResponse(call: Call<List<HistoryEntryDTO>>, response: Response<List<HistoryEntryDTO>>) {
                Log.d("call", response.message())
                val body = response.body()
                if (body != null) {
                    listOfHistory.clear()
                    listOfHistory.addAll(body)
                    adapter.update(listOfHistory)
                }
            }
        })

        val recyclerView = findViewById<RecyclerView>(R.id.list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = HistoryAdapter(emptyList(),this)
        recyclerView.adapter = adapter
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(this, HistoryDetailsActivity::class.java)
        intent.putExtra("name", listOfHistory[position].name)
        intent.putExtra("totalTime", listOfHistory[position].date)
        intent.putExtra("price", listOfHistory[position].price)
        intent.putExtra("image", listOfHistory[position].image)
        intent.putExtra("lat", listOfHistory[position].latitude)
        intent.putExtra("lng", listOfHistory[position].longitude)
        startActivity(intent)
    }




}
