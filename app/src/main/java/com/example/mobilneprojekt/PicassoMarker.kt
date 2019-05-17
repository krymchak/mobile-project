package com.example.mobilneprojekt

import com.google.android.gms.maps.model.Marker
import android.graphics.drawable.Drawable
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.squareup.picasso.Picasso
import android.graphics.Bitmap
import java.lang.Exception


class PicassoMarker internal constructor(internal var mMarker: Marker) : com.squareup.picasso.Target {

    override fun hashCode(): Int {
        return mMarker.hashCode()
    }

    override fun equals(o: Any?): Boolean {
        if (o is PicassoMarker) {
            val marker = o.mMarker
            return mMarker == marker
        } else {
            return false
        }
    }

    override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
        mMarker.setIcon(BitmapDescriptorFactory.fromBitmap(bitmap))
    }

    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
    }

    override fun onPrepareLoad(placeHolderDrawable: Drawable) {

    }
}