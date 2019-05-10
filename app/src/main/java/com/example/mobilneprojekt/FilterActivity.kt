package com.example.mobilneprojekt

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.CheckBox
import android.widget.EditText
import kotlinx.android.synthetic.main.filter_activity.*
import java.lang.Integer.MAX_VALUE

class FilterActivity : AppCompatActivity()
{

    val sizes = arrayOf(2,4,5,7,9,22)
    val types = arrayOf("A", "B", "C", "D")

    var size = 40
    var uncheckedTypes = arrayListOf<String>()

    var minPrice = 0
    var maxPrice = MAX_VALUE


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.filter_activity)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, itemSelected: View, selectedItemPosition: Int, selectedId: Long)
            {
                val choose = resources.getStringArray(R.array.seats)
                size = choose[selectedItemPosition].toInt()
                /*if (choose[selectedItemPosition]=="Według odleglosci")
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
                }*/


            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    fun onClick(v: View)
    {
        //checkSize()
        checkType()
        borderOfPrice()

        val array = arrayOfNulls<String>(uncheckedTypes.size)
        uncheckedTypes.toArray(array)
        intent.putExtra("uncheckedTypes", array)
        intent.putExtra("size", size)
        intent.putExtra("minPrice", minPrice)
        intent.putExtra("maxPrice", maxPrice)
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun borderOfPrice()
    {
        val min = findViewById(R.id.min) as EditText
        val minValue = min.text.toString()
        if(minValue.trim().length>0)
        {
            minPrice = minValue.toInt()
        }

        val max = findViewById(R.id.max) as EditText
        val maxValue = max.text.toString()
        if(maxValue.trim().length>0)
        {
            maxPrice = maxValue.toInt()
        }
    }

    private fun checkType ()
    {
        for (i in 0..types.size-1)
        {
            val ID = types[i]
            val resID = resources.getIdentifier("type$ID", "id", packageName)
            val checkBox = findViewById(resID) as CheckBox
            if (!checkBox.isChecked)
            {
                uncheckedTypes.add(types[i])
                Log.v("aaaa", i.toString())
            }

        }
    }

   /* private fun checkType()
    {
        for (i in 0..sizes.size-1)
        {
            val ID = sizes[i]
            val resID = resources.getIdentifier("size$ID", "id", packageName)
            val checkBox = findViewById(resID) as CheckBox
            if (!checkBox.isChecked)
            {
                uncheckedSizes.add(sizes[i])
            }
        }
    }*/
}