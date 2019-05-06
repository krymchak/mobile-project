package com.example.mobilneprojekt

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import java.lang.Integer.MAX_VALUE

class FilterActivity : AppCompatActivity()
{

    val sizes = arrayOf(2,4,5,7,9,22)
    val types = arrayOf("A", "B", "C", "D")

    var checkedSizes = arrayListOf<Int>()
    var checkedTypes = arrayListOf<String>()

    var minPrice = 0
    var maxPrice = MAX_VALUE


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.filter_activity)
    }

    fun onClick(v: View)
    {
        checkSize()
        checkType()
        borderOfPrice()

        intent.putExtra("checkedTypes", checkedTypes)
        intent.putExtra("checkedSizes", checkedSizes)
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

    private fun checkSize ()
    {
        for (i in 0..types.size-1)
        {
            val ID = types[i]
            val resID = resources.getIdentifier("type$ID", "id", packageName)
            val checkBox = findViewById(resID) as CheckBox
            if (checkBox.isChecked)
            {
                checkedTypes.add(types[i])
            }

        }
    }

    private fun checkType()
    {
        for (i in 0..sizes.size-1)
        {
            val ID = sizes[i]
            val resID = resources.getIdentifier("size$ID", "id", packageName)
            val checkBox = findViewById(resID) as CheckBox
            if (checkBox.isChecked)
            {
                checkedSizes.add(sizes[i])
            }
        }
    }
}