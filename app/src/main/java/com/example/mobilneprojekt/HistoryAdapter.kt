package com.example.mobilneprojekt

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.mobilneprojekt.services.HistoryEntryDTO
import com.example.mobilneprojekt.services.ServiceBuilder
import com.squareup.picasso.Picasso

class HistoryAdapter(private var values: List<HistoryEntryDTO>, private var clickListener: ClickListener, private var context: Context): RecyclerView.Adapter<HistoryAdapter.ViewHolder>()
{
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.vehicle.text = values[position].name
        holder.date.text = values[position].date
        holder.price.text = context.getString(R.string.priceFormat, values[position].price)

        val url = "${ServiceBuilder.getUrl()}${values[position].image}"
        Picasso.get().load(url).centerCrop().fit().into(holder.image)

    }

    override fun getItemCount(): Int {
        return values.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false)
        return ViewHolder(itemView, clickListener)
    }

    class ViewHolder(view: View, private var clickListener: ClickListener) : RecyclerView.ViewHolder(view), View.OnClickListener
    {
        var vehicle = view.findViewById(R.id.vehicle) as TextView
        var date = view.findViewById(R.id.date) as TextView
        var price = view.findViewById(R.id.cost) as TextView
        var image = view.findViewById(R.id.imageVehicle) as ImageView

        init
        {
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