package com.example.mobilneprojekt

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.history_item.view.*

class HistoryAdapter(val items: ArrayList<HistoryItem>, val clickListener: (Int) -> Unit) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.date.text = item.date
        holder.startTime.text = item.startTime
        holder.imageVehicle.setImageResource(item.vehicleImage)
        holder.vechicleName.text = item.info.getName()
        holder.totalTime.text = item.totalTime
        holder.cost.text = item.info.getPrice().toString()
        holder.maybeMap.setOnClickListener{
            clickListener(position)
        }

    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val date: TextView = view.date
        val startTime: TextView = view.timeStart
        val imageVehicle: ImageView = view.imageVehicle
        val vechicleName: TextView = view.vehicle
        val totalTime: TextView = view.timeTrip
        val cost: TextView = view.cost
        val maybeMap: ImageView = view.map
    }


}