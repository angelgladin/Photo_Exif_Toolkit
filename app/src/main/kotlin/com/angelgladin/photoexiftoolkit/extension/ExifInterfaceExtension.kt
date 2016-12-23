package com.angelgladin.photoexiftoolkit.extension

import android.media.ExifInterface

/**
 * Created on 12/22/16.
 */
fun ExifInterface.getMap(): MutableMap<String, String>? {
  val mAttributesField = this.javaClass.getDeclaredField("mAttributes")
  mAttributesField.isAccessible = true

  @Suppress("UNCHECKED_CAST")
  val map: MutableMap<String, String> = mAttributesField.get(this) as MutableMap<String, String>

  val latLonArray = FloatArray(2)
  if (this.getLatLong(latLonArray)) {
    map["Latitude"] = latLonArray[0].toString()
    map["Longitude"] = latLonArray[1].toString()
  }

  return map
}