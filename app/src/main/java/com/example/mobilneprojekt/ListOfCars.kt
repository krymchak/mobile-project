package com.example.mobilneprojekt

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import com.example.mobilneprojekt.services.CarDTO
import com.example.mobilneprojekt.services.ServiceBuilder
import kotlinx.android.synthetic.main.list_of_cars.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ListOfCars : AppCompatActivity(), Adapter.ClickListener {

    var listOfCars = ArrayList<CarDTO>()
    var filterListOfCars = ArrayList<CarDTO>()
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
//        listOfCars.add(CarDTO(1, "pojazd 1", "2018", 2000, 5, 3000, "B", ""))
//        listOfCars.add(CarDTO(2, "pojazd 2", "2008", 3499, 4, 3000, "B", ""))
//        listOfCars.add(CarDTO(3, "pojazd 3", "2010", 11999, 20, 3000, "C", ""))
//        listOfCars.add(CarDTO(4, "pojazd 4", "2000", 4000, 4, 3000, "B", ""))
        // filterListOfCars = listOfCars.clone() as ArrayList<CarDTO>
        val recyclerView = findViewById<RecyclerView>(R.id.list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = Adapter(emptyList(),this)
        recyclerView.adapter = adapter

    }

    override fun onItemClick(position: Int) {
        val intent = Intent(this, DetailInfoCarActivity::class.java)
        intent.putExtra("name", filterListOfCars[position].name)
        intent.putExtra("category", filterListOfCars[position].category)
        intent.putExtra("year", filterListOfCars[position].year)
        intent.putExtra("seats", filterListOfCars[position].seats.toString())
        intent.putExtra("dmc", filterListOfCars[position].dmc.toString())
        intent.putExtra("mileage", filterListOfCars[position].mileage.toString())
        startActivity(intent)
    }

    private fun sortByPriceFromBiggest() {
        //var list= filterListOfCars.sortedWith(compareByDescending({ it.getPrice() }))
        //adapter.update(list)
    }

    private fun sortByPriceFromSmallest() {
        //var list= filterListOfCars.sortedWith(compareBy({ it.getPrice() }))
       // adapter.update(list)
    }

    private fun sortByDistance() {

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
            while (i<filterListOfCars.size)
            {
                if(filterListOfCars[i].category==category[j])
                {
                    filterListOfCars.removeAt(i)
                    i--
                }
                i++
            }
        }
    }

    fun filterBySize(size: Int)
    {
        var i=0
        while (i<filterListOfCars.size)
        {
            if(filterListOfCars[i].seats<size)
            {
                filterListOfCars.removeAt(i)
                i--

            }
            i++
        }
    }

    fun filterByPrice(minPrice: Int, maxPrice: Int)
    {
        /*var i=0
        while (i<filterListOfCars.size)
        {
            if(filterListOfCars[i].getPrice()<minPrice.toFloat() || filterListOfCars[i].getPrice()>maxPrice)
            {
                filterListOfCars.removeAt(i)
                i--

            }
            i++
        }*/
    }

    fun filterList(minPrice: Int, maxPrice: Int, category: Array<String>, size: Int)
    {
        filterListOfCars = listOfCars.clone() as ArrayList<CarDTO>
        filterByCategory(category)
        filterBySize(size)
        filterByPrice(minPrice, maxPrice)
        adapter.update(filterListOfCars)
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
            R.id.History -> {
                Intent(this, HistoryListActivity::class.java).apply {
                    startActivity(this)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

}