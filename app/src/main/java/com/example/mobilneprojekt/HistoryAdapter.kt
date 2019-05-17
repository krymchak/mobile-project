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
import com.example.mobilneprojekt.services.HistoryEntryDTO
import com.example.mobilneprojekt.services.ServiceBuilder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.history_item.view.*

class HistoryAdapter(values: List<HistoryEntryDTO>, clickListener: ClickListener): RecyclerView.Adapter<HistoryAdapter.ViewHolder>()
{

    var values: List<HistoryEntryDTO>
    var clickListener: ClickListener

    init
    {
        this.values = values
        this.clickListener = clickListener
    }
    override fun onBindViewHolder(holder: HistoryAdapter.ViewHolder, position: Int) {
        holder.vehicle.text = values[position].name
        holder.date.text = values[position].date
        holder.price.text = values[position].price.toString()

        val url = "${ServiceBuilder.getUrl()}${values[position].image}"

    }

    override fun getItemCount(): Int {
        return values.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)
        return ViewHolder(itemView, clickListener)
    }

    class ViewHolder(view: View, clickListener: ClickListener) : RecyclerView.ViewHolder(view), View.OnClickListener
    {
        var vehicle = view.findViewById(R.id.vehicle) as TextView
        var date = view.findViewById(R.id.date) as TextView
        var price = view.findViewById(R.id.price) as TextView

        var clickListener: ClickListener

        init
        {

            this.clickListener=clickListener
            view.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            clickListener.onItemClick(adapterPosition)
        }
    }

    fun update(results: List<HistoryEntryDTO>) {
        values = results
        notifyDataSetChanged()
    }

    interface ClickListener {
        fun onItemClick(position: Int)
    }


}