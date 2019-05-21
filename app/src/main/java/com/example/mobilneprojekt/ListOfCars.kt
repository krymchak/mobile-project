package com.example.mobilneprojekt

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.mobilneprojekt.services.CarDTO
import com.example.mobilneprojekt.services.ServiceBuilder
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val REQUEST_TAKE_PHOTO = 101
const val REQUEST_ADD_CAR = 102
const val REQUEST_FILTER = 103

class ListOfCars : AppCompatActivity(), Adapter.ClickListener {

    var listOfCars = ArrayList<CarDTO>()
    lateinit var adapter: Adapter
    var numberOfNewActivity=2
    lateinit var token : String
    var category: Array<String> = arrayOf()
    var size = Integer.MAX_VALUE
    var minPrice = 0
    var maxPrice = Integer.MAX_VALUE
    lateinit var preferences: SharedPreferences
    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_of_cars)
        preferences = getSharedPreferences("com.herokuapp.mobilne-projekt", Context.MODE_PRIVATE)

        swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.pullToRefresh).apply {
            setOnRefreshListener {
                loadData()
            }
        }
        loadData()
        val recyclerView = findViewById<RecyclerView>(R.id.list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = Adapter(emptyList(),this)
        recyclerView.adapter = adapter


    }

    override fun onResume() {
        super.onResume()
        loadData()
    }


    fun loadData()
    {
        swipeRefreshLayout.isRefreshing = true
        token = preferences.getString("token", "") ?: ""
        if (token == "") {
            finish()
        }
        val callCars = ServiceBuilder.getRentalService().getCars(token)
        callCars.enqueue(object : Callback<List<CarDTO>> {
            override fun onFailure(call: Call<List<CarDTO>>, t: Throwable) {
                Log.e("call", "Failed to get list of cars")
                logout()
            }

            override fun onResponse(call: Call<List<CarDTO>>, response: Response<List<CarDTO>>) {
                Log.d("call", response.message())
                val body = response.body()
                if (body != null) {
                    listOfCars.clear()
                    listOfCars.addAll(body)
                    adapter.update(listOfCars)
                }
                swipeRefreshLayout.isRefreshing = false
            }
        })

    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        if (numberOfNewActivity==1)
            loadData()
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(this, DetailInfoCarActivity::class.java)

        intent.putExtra("id", listOfCars[position].id)
        intent.putExtra("name", listOfCars[position].name)
        intent.putExtra("category", listOfCars[position].category)
        intent.putExtra("year", listOfCars[position].year)
        intent.putExtra("seats", listOfCars[position].seats.toString())
        intent.putExtra("dmc", listOfCars[position].dmc.toString())
        intent.putExtra("mileage", listOfCars[position].mileage.toString())
        intent.putExtra("price", listOfCars[position].price.toString())
        intent.putExtra("image", listOfCars[position].image)
        numberOfNewActivity=2
        startActivityForResult(intent,2)
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
        intent.putExtra("category", category)
        intent.putExtra("size", size)
        intent.putExtra("minPrice", minPrice)
        intent.putExtra("maxPrice", maxPrice)
        numberOfNewActivity=1
        startActivityForResult(intent, REQUEST_FILTER)
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
        when(requestCode) {
            REQUEST_TAKE_PHOTO -> {
                Log.d("ar", "REQUEST_TAKE_PHOTO returned")
                if (resultCode == RESULT_OK && data != null) {
                    Log.d("ar", "got data from camera")
                    val imageBitmap = data.extras.get("data") as Bitmap
                    Intent(this, AddCarActivity::class.java).also { addCarIntent ->
                        addCarIntent.putExtra("data", imageBitmap)
                        addCarIntent.putExtra("token", token)
                        startActivityForResult(addCarIntent, REQUEST_ADD_CAR)
                    }
                }
            }
            REQUEST_FILTER -> {
                if (data == null) {

                    return
                }
                //if(resultCode==1)
                //{
                category = data.getStringArrayExtra("uncheckedTypes")
                size = data.getIntExtra("size", 0)
                minPrice = data.getIntExtra("minPrice", 0)
                maxPrice = data.getIntExtra("maxPrice", 0)
                filterList(minPrice, maxPrice, category, size)
                //}
            }
        }
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
                Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                    takePictureIntent.resolveActivity(packageManager)?.also {
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                    }
                }
            }
            R.id.info -> {
                Log.v("am", "info")
                Intent(this, ReturnCarActivity::class.java).apply {
                    startActivity(this)
                }
            }
            R.id.map -> {
                val intent = Intent(this, MapActivity::class.java)
                intent.putExtra("size", listOfCars.size)
                val gson = Gson()
                var json : String
                for (i in 0..listOfCars.size-1)
                {
                    json = gson.toJson(listOfCars[i])
                    intent.putExtra(i.toString(), json)
                }
                numberOfNewActivity=3

                startActivityForResult(intent,3)
            }
            R.id.logout -> {
                logout()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logout() {
        with (preferences.edit()) {
            putString("token", "")
            apply()
        }
        finish()
    }



}