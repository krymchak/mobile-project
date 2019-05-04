package com.example.mobilneprojekt

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ListView
import kotlinx.android.synthetic.main.list_of_cars.*

class ListOfCars : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_of_cars)

        val listOfCars = ArrayList<CarsInfo>()
        listOfCars.add(CarsInfo("pojazd 1", "A", 30.0F))
        listOfCars.add(CarsInfo("pojazd 2", "B", 60.0F))
        listOfCars.add(CarsInfo("pojazd 3", "C", 35.49F))
        listOfCars.add(CarsInfo("pojazd 4", "D", 100.0F))
        //listOfCars.add("2")
        //listOfCars.add("3")
        //listOfCars.add("4")
        val recyclerView = findViewById<RecyclerView>(R.id.list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = Adapter(listOfCars)
    }


}