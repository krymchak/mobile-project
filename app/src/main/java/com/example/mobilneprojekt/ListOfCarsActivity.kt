package com.example.mobilneprojekt

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.support.v4.widget.DrawerLayout
import android.support.design.widget.NavigationView
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import com.example.mobilneprojekt.services.CarDTO
import com.example.mobilneprojekt.services.ServiceBuilder
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

const val REQUEST_TAKE_PHOTO = 101
const val REQUEST_ADD_CAR = 102
const val REQUEST_FILTER = 103

class ListOfCarsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var listOfCars = ArrayList<CarDTO>()
    private lateinit var adapter: Adapter
    private var numberOfNewActivity = 2
    private lateinit var token: String
    private var category: Array<String> = arrayOf()
    private var size = 0
    private var minPrice = 0
    private var maxPrice = Integer.MAX_VALUE
    private var sorted = false
    private var desc = false
    private lateinit var preferences: SharedPreferences
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_of_cars)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

        preferences = getSharedPreferences("com.herokuapp.mobilne-projekt", Context.MODE_PRIVATE)

        val recyclerView = findViewById<RecyclerView>(R.id.list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val context = this
        adapter = Adapter(emptyList(), object : Adapter.ClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(context, DetailInfoCarActivity::class.java)
                intent.putExtra("id", listOfCars[position].id)
                intent.putExtra("name", listOfCars[position].name)
                intent.putExtra("category", listOfCars[position].category)
                intent.putExtra("year", listOfCars[position].year)
                intent.putExtra("seats", listOfCars[position].seats.toString())
                intent.putExtra("dmc", listOfCars[position].dmc.toString())
                intent.putExtra("mileage", listOfCars[position].mileage.toString())
                intent.putExtra("price", listOfCars[position].price.toString())
                intent.putExtra("image", listOfCars[position].image)
                numberOfNewActivity = 2
                startActivityForResult(intent, 2)
            }
        }, this)
        recyclerView.adapter = adapter
        swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.pullToRefresh).apply {
            setOnRefreshListener {
                onRefresh()
            }
        }
        loadData()
    }

    private fun onRefresh() {
        loadData()
    }

    override fun onResume() {
        super.onResume()
        onRefresh()
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        if (numberOfNewActivity == 1)
            loadData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_TAKE_PHOTO -> {
                Log.d("ar", "REQUEST_TAKE_PHOTO returned")
                if (resultCode == RESULT_OK && data != null) {
                    Log.d("ar", "got data from camera")
                    val imageBitmap = data.extras?.get("data") as Bitmap
                    Intent(this, AddCarActivity::class.java).also { addCarIntent ->
                        addCarIntent.putExtra("data", imageBitmap)
                        addCarIntent.putExtra("token", token)
                        startActivityForResult(addCarIntent, REQUEST_ADD_CAR)
                    }
                }
            }
            REQUEST_FILTER -> {
                if (data != null) {
                    category = data.getStringArrayExtra("uncheckedTypes")
                    Log.d("toto", Arrays.toString(category))
                    size = data.getIntExtra("size", 0)
                    minPrice = data.getIntExtra("minPrice", 0)
                    maxPrice = data.getIntExtra("maxPrice", 0)
                    filterList()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.list_of_cars, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.sortByPriceFromSmallest -> {
                sorted = true
                desc = false
                onRefresh()
                true
            }
            R.id.sortByPriceFromBiggest -> {
                sorted = true
                desc = true
                onRefresh()
                true
            }
            R.id.Filter -> {
                filter()
                true
            }
            R.id.map -> {
                val intent = Intent(this, MapActivity::class.java)
                intent.putExtra("size", listOfCars.size)
                var json: String
                for (i in 0 until listOfCars.size) {
                    json = Gson().toJson(listOfCars[i])
                    intent.putExtra(i.toString(), json)
                }
                numberOfNewActivity = 3

                startActivityForResult(intent, 3)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_rented -> {
                Log.v("click", "rented")
                Intent(this, ReturnCarActivity::class.java).apply {
                    startActivity(this)
                }
            }
            R.id.nav_add -> {
                Log.v("click", "add")
                Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                    takePictureIntent.resolveActivity(packageManager)?.also {
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                    }
                }
            }
            R.id.nav_history -> {
                Log.v("click", "history")
                Intent(this, HistoryListActivity::class.java).apply {
                    startActivity(this)
                }
            }
            R.id.nav_my -> {
                Log.v("click", "my")
                Intent(this, MyCarsActivity::class.java).apply {
                    startActivity(this)
                }
            }
            R.id.nav_logout -> {
                Log.v("click", "logout")
                logout()
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun logout() {
        with(preferences.edit()) {
            putString("token", "")
            apply()
        }
        finish()
    }

    private fun filter() {
        val intent = Intent(this, FilterActivity::class.java)
        intent.putExtra("category", category)
        intent.putExtra("size", size)
        intent.putExtra("minPrice", minPrice)
        intent.putExtra("maxPrice", maxPrice)
        numberOfNewActivity = 1
        startActivityForResult(intent, REQUEST_FILTER)
    }

    private fun filterByCategory(category: Array<String>) {
        for (j in 0 until category.size) {
            var i = 0
            while (i < listOfCars.size) {
                if (listOfCars[i].category == category[j]) {
                    listOfCars.removeAt(i)
                    i--
                }
                i++
            }
        }
    }

    private fun filterBySize(size: Int) {
        var i = 0
        while (i < listOfCars.size) {
            if (listOfCars[i].seats < size) {
                listOfCars.removeAt(i)
                i--
            }
            i++
        }
    }

    private fun filterByPrice(minPrice: Int, maxPrice: Int) {
        var i = 0
        while (i < listOfCars.size) {
            if (listOfCars[i].price < minPrice.toFloat() || listOfCars[i].price > maxPrice) {
                listOfCars.removeAt(i)
                i--
            }
            i++
        }
    }

    private fun filterList() {
        filterByCategory(category)
        filterBySize(size)
        filterByPrice(minPrice, maxPrice)
        if (sorted) {
            if (desc) {
                sortByPriceFromBiggest()
            } else {
                sortByPriceFromSmallest()
            }
        }
        adapter.update(listOfCars)
    }

    private fun sortByPriceFromBiggest() {
        listOfCars.sortWith(compareByDescending { it.price })
        adapter.update(listOfCars)
    }

    private fun sortByPriceFromSmallest() {
        listOfCars.sortWith(compareBy { it.price })
        adapter.update(listOfCars)
    }

    private fun loadData() {
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
                    filterList()
                }
                swipeRefreshLayout.isRefreshing = false
            }
        })

    }
}
