package com.example.mobilneprojekt

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.mobilneprojekt.services.CarDTO

class Adapter(values: List<CarDTO>): RecyclerView.Adapter<Adapter.ViewHolder>()
{
    var values: List<CarDTO>

    init
    {
        this.values = values
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = values[position].name
        holder.type.text = values[position].category
        //holder.price.text = values[position].getPrice().toString() + "$"
        holder.price.text = "0$"
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

    fun update(results: List<CarDTO>) {
        values = results
        notifyDataSetChanged()
    }
}
