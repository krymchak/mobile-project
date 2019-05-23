package com.example.mobilneprojekt

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.example.mobilneprojekt.services.CarDTO
import com.example.mobilneprojekt.services.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyCarsActivity : AppCompatActivity() {

    private lateinit var preferences: SharedPreferences
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var adapter: MyCarsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_cars)

        preferences = getSharedPreferences("com.herokuapp.mobilne-projekt", Context.MODE_PRIVATE)
        swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.pullToRefresh).apply {
            setOnRefreshListener {
                onRefresh()
            }
        }
        adapter = MyCarsAdapter(emptyList(), object : MyCarsAdapter.ClickListener {
            override fun onItemClick(position: Int) {

            }
        }, this)
        findViewById<RecyclerView>(R.id.list).also {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = adapter
        }
    }

    override fun onResume() {
        super.onResume()
        onRefresh()
    }

    private fun onRefresh() {
        swipeRefreshLayout.isRefreshing = true
        val token = preferences.getString("token", "") ?: ""
        if (token == "") {
            finish()
            return
        }
        ServiceBuilder.getRentalService().getMyCars(token).enqueue(object : Callback<List<CarDTO>> {
            override fun onFailure(call: Call<List<CarDTO>>, t: Throwable) {
                Log.e("rest", t.message)
                swipeRefreshLayout.isRefreshing = false
            }

            override fun onResponse(call: Call<List<CarDTO>>, response: Response<List<CarDTO>>) {
                swipeRefreshLayout.isRefreshing = false
                val list: List<CarDTO> = response.body() ?: emptyList()
                adapter.update(list)
            }
        })
    }
}
