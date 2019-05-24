package com.example.mobilneprojekt

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.mobilneprojekt.services.CarDTO
import com.example.mobilneprojekt.services.CarExtDTO
import com.example.mobilneprojekt.services.ServiceBuilder
import com.squareup.picasso.Picasso

class MyCarsAdapter(
    private var values: List<CarExtDTO>,
    private var clickListener: ClickListener,
    private var context: Context
) : RecyclerView.Adapter<MyCarsAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = values[position].name
        holder.type.text = values[position].category
        holder.price.text = context.getString(R.string.priceFormat, values[position].price)
        val url = "${ServiceBuilder.getUrl()}${values[position].image}"
        Picasso.get().load(url).placeholder(R.drawable.load).centerCrop().fit().into(holder.image)

        holder.rentedView.text = if (values[position].isRented) context.getString(R.string.rented) else ""
    }

    override fun getItemCount(): Int {
        return values.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.my_cars_row, parent, false)
        return ViewHolder(itemView, clickListener)
    }

    class ViewHolder(view: View, private var clickListener: ClickListener) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        var name: TextView = view.findViewById(R.id.name)
        var type: TextView = view.findViewById(R.id.typeB)
        var price: TextView = view.findViewById(R.id.price)
        var image: ImageView = view.findViewById(R.id.imageView2)
        var rentedView: TextView = view.findViewById(R.id.rented)

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            Log.v("click", "onClick: $adapterPosition")
            clickListener.onItemClick(adapterPosition)
        }
    }

    fun update(results: List<CarExtDTO>) {
        values = results
        notifyDataSetChanged()
    }

    interface ClickListener {
        fun onItemClick(position: Int)
    }
}
