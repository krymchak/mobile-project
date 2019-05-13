package com.example.mobilneprojekt

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.mobilneprojekt.services.CarDTO
import com.example.mobilneprojekt.services.ServiceBuilder
import com.squareup.picasso.Picasso
import java.io.File

class Adapter(values: List<CarDTO>, clickListener: ClickListener): RecyclerView.Adapter<Adapter.ViewHolder>()
{
    var values: List<CarDTO>
    var clickListener: ClickListener

    init
    {
        this.values = values
        this.clickListener = clickListener;
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = values[position].name
        holder.type.text = values[position].category
        //holder.price.text = values[position].getPrice().toString() + "$"
        holder.price.text = values[position].price.toString()
        val url = "${ServiceBuilder.getUrl()}${values[position].image}"
        Picasso.get().load(url).centerCrop().fit().into(holder.image)

    }

    override fun getItemCount(): Int {
        return values.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Adapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)
        return ViewHolder(itemView, clickListener)
    }

    class ViewHolder(view: View, clickListener: ClickListener) : RecyclerView.ViewHolder(view), View.OnClickListener
    {
        var name: TextView
        var type : TextView
        var price: TextView
        var image: ImageView
        var clickListener: ClickListener
        init
        {
                name = view.findViewById(R.id.name)
                type = view.findViewById(R.id.type)
                price = view.findViewById(R.id.price)
                image = view.findViewById(R.id.imageView2)
                this.clickListener=clickListener
                view.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            Log.d("aaaa", "onClick: $adapterPosition")
            clickListener.onItemClick(adapterPosition)
        }
    }

    fun update(results: List<CarDTO>) {
        values = results
        notifyDataSetChanged()
    }

    interface ClickListener {
        fun onItemClick(position: Int)
    }


}
