package com.angelgladin.photoexiftoolkit.extension

import android.media.ExifInterface
import com.angelgladin.photoexiftoolkit.util.Constants
import java.util.*

/**
 * Created on 12/22/16.
 */
@Suppress("UNCHECKED_CAST")
fun ExifInterface.getMap(): HashMap<String, String> {
    val mAttributesField = this.javaClass.getDeclaredField("mAttributes")
    mAttributesField.isAccessible = true
    val mAttributes = mAttributesField.get(this)

    var map = HashMap<String, String>()
    if (mAttributes is Array<*>) {
        val arrayOfMapAux = mAttributes as Array<HashMap<String, *>>
        arrayOfMapAux.indices
                .flatMap { mAttributes[it].entries }
                .forEach { map[it.key] = this.getAttribute(it.key) }
    } else if (mAttributes is HashMap<*, *>) {
        map = mAttributes as HashMap<String, String>
    }

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

