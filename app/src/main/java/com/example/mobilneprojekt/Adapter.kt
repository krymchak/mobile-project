package com.example.mobilneprojekt

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class Adapter(values: List<CarsInfo>): RecyclerView.Adapter<Adapter.ViewHolder>()
{
    var values: List<CarsInfo>

    init
    {
        this.values = values
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = values[position].getName()
        holder.type.text = values[position].getType()
        holder.price.text = values[position].getPrice().toString() + "$"
    }

    override fun getItemCount(): Int {
        return values.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Adapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)
        return ViewHolder(itemView)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        var name: TextView
        var type : TextView
        var price: TextView
        init
        {
                name = view.findViewById(R.id.name)
                type = view.findViewById(R.id.type)
                price = view.findViewById(R.id.price)
        }
    }

    fun update(results: List<CarsInfo>) {
        values = results
        notifyDataSetChanged()
    }
}
    /*var listOfCars: ArrayList<String> = ArrayList<String>()
    private val mContext: Context

    init
    {
        mContext=context
        listOfCars=list
    }
    override fun getCount(): Int
    {
        return listOfCars.size
    }

    override fun getItemId(position: Int): Long
    {
        return position.toLong()
    }

    override fun getItem(position: Int): Any
    {
        return "Test"
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View
    {
        val layout = LayoutInflater.from(mContext)
        val row = layout.inflate(R.layout.row, parent, false)
        row.findViewById<TextView>(R.id.textView).text=listOfCars[position]

        return row
    }

    fun updateResults(results: ArrayList<String>) {
        listOfCars = results
        notifyDataSetChanged()
    }*/
