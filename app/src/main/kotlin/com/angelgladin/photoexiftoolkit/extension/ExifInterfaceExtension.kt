/**
 * Photo EXIF Toolkit for Android.
 *
 * Copyright (C) 2017 Ángel Iván Gladín García
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


package com.angelgladin.photoexiftoolkit.extension

import android.media.ExifInterface
import com.angelgladin.photoexiftoolkit.util.Constants
import java.util.*

//TODO: catch exceptions
private fun ExifInterface.mAttributes(): Any {
    val mAttributesField = this.javaClass.getDeclaredField("mAttributes")
    mAttributesField.isAccessible = true
    return mAttributesField.get(this)
}

/**
 * Created on 12/22/16.
 */
@Suppress("UNCHECKED_CAST")
fun ExifInterface.getTags(): HashMap<String, String> {
    val mAttributes = mAttributes()

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

fun ExifInterface.removeAllTags() {
    val mAttributes = mAttributes()

    if (mAttributes is Array<*>) {
        val arrayOfMapAux = mAttributes as Array<HashMap<String, *>>
        arrayOfMapAux.forEach { map -> map.clear() }

    } else if (mAttributes is HashMap<*, *>) {
        val map = mAttributes as HashMap<String, String>
        map.clear()
    }
    this.saveAttributes()
}

/**
 * Ok, this is very tricky
 */
fun ExifInterface.removeTags(tags: Set<String>) {
    val mAttributes = mAttributes()

    if (mAttributes is Array<*>) {
        val arrayOfMapAux = mAttributes as Array<HashMap<String, *>>
        arrayOfMapAux.forEach { map ->
            map.keys.filter { it in tags }
                    .forEach { key -> map.remove(key) }
        }

    } else if (mAttributes is HashMap<*, *>) {
        val map = mAttributes as HashMap<String, String>
        map.keys.filter { it in tags }
                .forEach { map.remove(it) }
    }
    this.saveAttributes()
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

fun ExifInterface.getLongitudeRef(longitude: Double): String =
        if (longitude < 0.0) "W" else "E"

