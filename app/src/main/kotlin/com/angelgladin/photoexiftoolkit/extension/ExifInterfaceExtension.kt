package com.angelgladin.photoexiftoolkit.extension

import android.media.ExifInterface
import com.angelgladin.photoexiftoolkit.util.Constants

/**
 * Created on 12/22/16.
 */
fun ExifInterface.getMap(): MutableMap<String, String> {
    val mAttributesField = this.javaClass.getDeclaredField("mAttributes")
    mAttributesField.isAccessible = true

    @Suppress("UNCHECKED_CAST")
    val map: MutableMap<String, String> = mAttributesField.get(this) as MutableMap<String, String>

    val latLonArray = FloatArray(2)
    if (this.getLatLong(latLonArray)) {
        map[Constants.EXIF_LATITUDE] = latLonArray[0].toString()
        map[Constants.EXIF_LONGITUDE] = latLonArray[1].toString()
    }

    return map
}

fun ExifInterface.convertDecimalToDegrees(decimal: Double): String {
    var latitude = Math.abs(decimal)
    val degree = latitude.toInt()
    latitude *= 60
    latitude -= (degree * 60.0)
    val minute = latitude.toInt()
    latitude *= 60
    latitude -= (minute * 60.0)
    val second = (latitude * 1000.0).toInt()
    return "$degree/1,$minute/1,$second/1000"
}

fun ExifInterface.getLatitudeRef(latitude: Double): String =
        if (latitude < 0.0) "S" else "N"

fun ExifInterface.getLongitudeRef(latitude: Double): String =
        if (latitude < 0.0) "W" else "E"

