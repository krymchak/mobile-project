package com.example.mobilneprojekt

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
    var filterListOfCars = ArrayList<HistoryEntryDTO>()
    lateinit var adapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_of_cars)

        val callCars = ServiceBuilder.getRentalService().getHistory("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJsb2dpbiI6ImFkbWluIiwiaWF0IjoxNTU3NzQ2MjU0LCJleHAiOjE1NzA3MDYyNTR9.oaSsRbNO4vio9xkvEG70L-DcJ6LsDPaRyM_hxh3uAfU")
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

        startActivity(intent)
    }


}
