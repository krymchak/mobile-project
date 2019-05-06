package com.example.mobilneprojekt

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import kotlinx.android.synthetic.main.list_of_cars.*



class ListOfCars : AppCompatActivity() {

    val listOfCars = ArrayList<CarsInfo>()
    var filterListOfCars = ArrayList<CarsInfo>()
    lateinit var adapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_of_cars)

        listOfCars.add(CarsInfo("pojazd 1", "A", 30.0F))
        listOfCars.add(CarsInfo("pojazd 2", "B", 60.0F))
        listOfCars.add(CarsInfo("pojazd 3", "C", 35.49F))
        listOfCars.add(CarsInfo("pojazd 4", "D", 100.0F))
        filterListOfCars = listOfCars.clone() as ArrayList<CarsInfo>
        val recyclerView = findViewById<RecyclerView>(R.id.list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = Adapter(filterListOfCars)
        recyclerView.setAdapter(adapter)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, itemSelected: View, selectedItemPosition: Int, selectedId: Long)
            {
                val choose = resources.getStringArray(R.array.typeOfSort)
                if (choose[selectedItemPosition]=="Według odleglosci")
                {
                    sortByDistance()
                }
                if (choose[selectedItemPosition]=="Według ceny (od najniższej)")
                {
                    sortByPriceFromSmallest()
                }
                if (choose[selectedItemPosition]=="Według ceny (od najwyższej)")
                {
                    sortByPriceFromBiggest()
                }


            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun sortByPriceFromBiggest() {
        var list= filterListOfCars.sortedWith(compareByDescending({ it.getPrice() }))
        adapter.update(list)
    }

    private fun sortByPriceFromSmallest() {
        var list= filterListOfCars.sortedWith(compareBy({ it.getPrice() }))
        adapter.update(list)
    }

    private fun sortByDistance() {

    }

    fun filter (v:View)
    {
        val intent = Intent(this, FilterActivity::class.java)
        startActivityForResult(intent, 1)
    }

    fun filterList(minPrice: Int, maxPrice: Int)
    {
        filterListOfCars = listOfCars.clone() as ArrayList<CarsInfo>
        var i=0
        while (i<filterListOfCars.size)
        {
            if(filterListOfCars[i].getPrice()<minPrice.toFloat() || filterListOfCars[i].getPrice()>maxPrice)
            {
                filterListOfCars.removeAt(i)
                i--

            }
            i++
        }
        adapter.update(filterListOfCars)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data == null) {

            return
        }
        val types = data.getStringArrayExtra("checkedTypes")
        val sizes = data.getIntArrayExtra("checkedSizes")
        val minPrice = data.getIntExtra("minPrice",0)
        val maxPrice = data.getIntExtra("maxPrice",0)
        filterList(minPrice,maxPrice)
        /*if (data == null) {

            return
        }
        val tytul = data.getStringExtra("tytul")
        val type = data.getStringExtra("typ")
        val time = data.getIntExtra("timer",0)
        val priority = data.getIntExtra("pryorytet",0)
        addTask(tytul, time, type, priority)*/



    }

}