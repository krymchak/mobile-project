package com.example.mobilneprojekt

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.filter_activity.*
import java.lang.Integer.MAX_VALUE


class FilterActivity : AppCompatActivity()
{

    val sizes = arrayOf(2,4,5,7,9,22)
    val types = arrayOf("A", "B", "C", "D")

    var size = 40
    var uncheckedTypes= ArrayList<String>()

    var minPrice = 0
    var maxPrice = MAX_VALUE


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.filter_activity)

        setData()

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, itemSelected: View, selectedItemPosition: Int, selectedId: Long)
            {
                val choose = resources.getStringArray(R.array.seats)
                size = choose[selectedItemPosition].toInt()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    fun setData()
    {
        val array = intent.getStringArrayExtra("category")
        for (i in 0..array.size-1)
        {
            uncheckedTypes.add(array[i])
        }
        size = intent.getIntExtra("size", 40)
        minPrice= intent.getIntExtra("minPrice",0)
        maxPrice= intent.getIntExtra("maxPrice",MAX_VALUE)

        for (i in 0..types.size-1)
        {

            for (j in 0..uncheckedTypes.size-1)
            {
                if (types[i]==uncheckedTypes[j])
                {
                    val ID = types[i]
                    val resID = resources.getIdentifier("type$ID", "id", packageName)
                    val checkBox = findViewById(resID) as CheckBox
                    checkBox.isChecked=false
                }
            }

        }

        if (minPrice!=0) {
            val min = findViewById(R.id.min) as EditText
            min.setText(minPrice.toString(), TextView.BufferType.EDITABLE)
        }
        if (maxPrice!= MAX_VALUE) {
            val max = findViewById(R.id.max) as EditText
            max.setText(maxPrice.toString(), TextView.BufferType.EDITABLE)
        }

        spinner.setSelection(getIndex(spinner, size.toString()));

    }

    private fun getIndex(spinner: Spinner, size: String): Int {

        var index = 0

        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i) == size) {
                index = i
            }
        }
        return index
    }

    fun onClick(v: View)
    {
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
        uncheckedTypes.clear()
        for (i in 0..types.size-1)
        {
            val ID = types[i]
            val resID = resources.getIdentifier("type$ID", "id", packageName)
            val checkBox = findViewById(resID) as CheckBox
            if (!checkBox.isChecked)
            {
                uncheckedTypes.add(types[i])
            }

        }
    }
}