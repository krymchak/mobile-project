package com.example.mobilneprojekt

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.mobilneprojekt.services.CarDTO
import com.example.mobilneprojekt.services.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ListOfCars : AppCompatActivity(), Adapter.ClickListener {

    var listOfCars = ArrayList<CarDTO>()
    lateinit var adapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_of_cars)

        val callCars = ServiceBuilder.getRentalService().getCars("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJsb2dpbiI6ImFkbWluIiwiaWF0IjoxNTU3NzQ2MjU0LCJleHAiOjE1NzA3MDYyNTR9.oaSsRbNO4vio9xkvEG70L-DcJ6LsDPaRyM_hxh3uAfU")
        callCars.enqueue(object : Callback<List<CarDTO>> {
            override fun onFailure(call: Call<List<CarDTO>>, t: Throwable) {
                Log.e("call", "Failed to get list of cars")
            }

            override fun onResponse(call: Call<List<CarDTO>>, response: Response<List<CarDTO>>) {
                Log.d("call", response.message())
                val body = response.body()
                if (body != null) {
                    listOfCars.clear()
                    listOfCars.addAll(body)
                    adapter.update(listOfCars)
                }
            }
        })
        val recyclerView = findViewById<RecyclerView>(R.id.list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = Adapter(emptyList(),this)
        recyclerView.adapter = adapter

    }


    override fun onSaveInstanceState(savedInstanceState: Bundle?)
    {
        super.onSaveInstanceState(savedInstanceState)
        val callCars = ServiceBuilder.getRentalService().getCars("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJsb2dpbiI6ImFkbWluIiwiaWF0IjoxNTU3NzQ2MjU0LCJleHAiOjE1NzA3MDYyNTR9.oaSsRbNO4vio9xkvEG70L-DcJ6LsDPaRyM_hxh3uAfU")
        callCars.enqueue(object : Callback<List<CarDTO>> {
            override fun onFailure(call: Call<List<CarDTO>>, t: Throwable) {
                Log.e("call", "Failed to get list of cars")
            }

            override fun onResponse(call: Call<List<CarDTO>>, response: Response<List<CarDTO>>) {
                Log.d("call", response.message())
                val body = response.body()
                if (body != null) {
                    listOfCars.clear()
                    listOfCars.addAll(body)
                    adapter.update(listOfCars)
                }
            }
        })
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(this, DetailInfoCarActivity::class.java)
        intent.putExtra("name", listOfCars[position].name)
        intent.putExtra("category", listOfCars[position].category)
        intent.putExtra("year", listOfCars[position].year)
        intent.putExtra("seats", listOfCars[position].seats.toString())
        intent.putExtra("dmc", listOfCars[position].dmc.toString())
        intent.putExtra("mileage", listOfCars[position].mileage.toString())
        intent.putExtra("price", listOfCars[position].price.toString())
        intent.putExtra("image", listOfCars[position].image)
        startActivity(intent)
    }


    private fun sortByPriceFromBiggest() {
        var list= listOfCars.sortedWith(compareByDescending({ it.price }))
        adapter.update(list)
    }

    private fun sortByPriceFromSmallest() {
        var list= listOfCars.sortedWith(compareBy({ it.price }))
        adapter.update(list)
    }

    fun filter ()
    {
        val intent = Intent(this, FilterActivity::class.java)
        startActivityForResult(intent, 1)
    }

    fun filterByCategory(category: Array<String>)
    {
        for (j in 0..category.size-1)
        {
            var i=0
            while (i<listOfCars.size)
            {
                if(listOfCars[i].category==category[j])
                {
                    listOfCars.removeAt(i)
                    i--
                }
                i++
            }
        }
    }

    fun filterBySize(size: Int)
    {
        var i=0
        while (i<listOfCars.size)
        {
            if(listOfCars[i].seats<size)
            {
                listOfCars.removeAt(i)
                i--

            }
            i++
        }
    }

    fun filterByPrice(minPrice: Int, maxPrice: Int)
    {
        var i=0
        while (i<listOfCars.size)
        {
            if(listOfCars[i].price<minPrice.toFloat() || listOfCars[i].price>maxPrice)
            {
                listOfCars.removeAt(i)
                i--

            }
            i++
        }
    }

    fun filterList(minPrice: Int, maxPrice: Int, category: Array<String>, size: Int)
    {
        listOfCars = listOfCars.clone() as ArrayList<CarDTO>
        filterByCategory(category)
        filterBySize(size)
        filterByPrice(minPrice, maxPrice)
        adapter.update(listOfCars)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data == null) {

            return
        }
        val category = data.getStringArrayExtra("uncheckedTypes")
        val size = data.getIntExtra("size",0)
        val minPrice = data.getIntExtra("minPrice",0)
        val maxPrice = data.getIntExtra("maxPrice",0)
        filterList(minPrice, maxPrice, category, size)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.list_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId)
        {
            R.id.sortByPriceFromSmallest -> {sortByPriceFromSmallest()}
            R.id.sortByPriceFromBiggest -> {sortByPriceFromBiggest()}
            R.id.Filter -> {filter()}
            R.id.history -> {
                Intent(this, HistoryListActivity::class.java).apply {
                    startActivity(this)
                }
            }
            R.id.add -> {
                Log.v("am", "add")
            }
            R.id.info -> {
                Log.v("am", "info")
            }
            /*R.id.map -> {
                Intent(this, HistoryListActivity::class.java).apply {
                    startActivity(this)
                }
            }*/
        }
        return super.onOptionsItemSelected(item)
    }



}