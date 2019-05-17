package com.example.mobilneprojekt

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.DrawableRes
import kotlinx.android.synthetic.main.activity_history_details.*
import android.graphics.*
import android.graphics.Bitmap
import android.view.ViewGroup
import android.app.Activity
import android.util.DisplayMetrics
import android.content.Context
import de.hdodenhof.circleimageview.CircleImageView
import android.view.LayoutInflater
import com.google.android.gms.maps.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.*


class HistoryDetailsActivity: AppCompatActivity(){

    lateinit var mapFragment: SupportMapFragment
    lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_details)

//        val id = intent.getStringExtra("id")
//        //get item from DB by ID.toInt()
//
        val from = LatLng(51.109687, 17.058089)
        val to = LatLng(51.103508, 17.085291)
//        val item = (HistoryItem(0,0, CarsInfo("CarName", "C", 12F), "12.12.12", "12.12.12","15:30",
//            "15:42", R.drawable.ic_launcher_background,  from, to,  "12H:12M:12S"))

        showItem()

        mapFragment = supportFragmentManager.findFragmentById(R.id.gMapFragment) as SupportMapFragment
        mapFragment.getMapAsync(OnMapReadyCallback {
            googleMap = it

            googleMap.addMarker(MarkerOptions().position(from).icon(BitmapDescriptorFactory.fromBitmap(createCustomMarker(this, R.drawable.s_marker))))
            googleMap.addMarker(MarkerOptions().position(to).icon(BitmapDescriptorFactory.fromBitmap(createCustomMarker(this, R.drawable.f_marker))))

            val builder = LatLngBounds.Builder()
            builder.include(from)
            builder.include(to)
            val bounds = builder.build()
            val cu = CameraUpdateFactory.newLatLngBounds(bounds, 300, 300, 0)
            googleMap.moveCamera(cu)
            googleMap.uiSettings.isMapToolbarEnabled = false
            googleMap.uiSettings.setAllGesturesEnabled(false)

            googleMap.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener {
                override fun onMarkerClick(marker: Marker): Boolean {
                    return true
                }
            })
        })

    }


    fun showItem() {
//        imageView.setImageResource(item.vehicleImage)
//        vname.text = item.info.getName()
//        id.text = item.id.toString()
//        date_start.text = item.date
//        start_time.text = item.startTime
//        start_coords.text = item.fromCoords.latitude.toString() + ", " + item.fromCoords.longitude.toString()
//        finish_coords.text = item.toCoords.latitude.toString() + ", " + item.toCoords.longitude.toString()
//        finish_date.text = item.finishDate
//        finish_time.text = item.endTime
//        total_time.text = item.totalTime
//        price.text = item.info.getPrice().toString()
        imageView
        vname.text = intent.getStringExtra("name")
    }

    fun createCustomMarker(context: Context, @DrawableRes resource: Int): Bitmap {

        val marker = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
            R.layout.custom_marker_layout,
            null
        )

        val markerImage = marker.findViewById(R.id.user_dp) as CircleImageView
        markerImage.setImageResource(resource)

        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        marker.layoutParams = ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT)
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels)
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)
        marker.buildDrawingCache()
        val bitmap = Bitmap.createBitmap(marker.measuredWidth, marker.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        marker.draw(canvas)

        return bitmap
    }



}