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
import com.example.mobilneprojekt.services.ServiceBuilder
import com.google.android.gms.maps.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.*
import com.squareup.picasso.Picasso


class HistoryDetailsActivity : AppCompatActivity() {

    private lateinit var mapFragment: SupportMapFragment
    private lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_details)

        val from = LatLng(intent.getDoubleExtra("lat", 0.0), intent.getDoubleExtra("lng", 0.0))
        showItem()

        mapFragment = supportFragmentManager.findFragmentById(R.id.gMapFragment) as SupportMapFragment
        mapFragment.getMapAsync {
            googleMap = it
            googleMap.addMarker(
                MarkerOptions().position(from).icon(
                    BitmapDescriptorFactory.fromBitmap(
                        createCustomMarker(this, R.drawable.s_marker)
                    )
                )
            )
            val builder = LatLngBounds.Builder()
            builder.include(from)
            val bounds = builder.build()
            val cu = CameraUpdateFactory.newLatLngBounds(bounds, 300, 300, 0)
            googleMap.moveCamera(cu)
            googleMap.uiSettings.isMapToolbarEnabled = false
            googleMap.uiSettings.setAllGesturesEnabled(false)
            googleMap.setOnMarkerClickListener { true }
        }

    }

    private fun showItem() {
        val url = "${ServiceBuilder.getUrl()}${intent.getStringExtra("image")}"
        Picasso.get().load(url).centerCrop().fit().into(imageView)
        vname.text = intent.getStringExtra("name")
        totalTime.text = intent.getStringExtra("totalTime")
        price.text = String.format("%.2fzł", intent.getFloatExtra("price", 0.0f))

    }

    @Suppress("DEPRECATION")
    private fun createCustomMarker(context: Context, @DrawableRes resource: Int): Bitmap {
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