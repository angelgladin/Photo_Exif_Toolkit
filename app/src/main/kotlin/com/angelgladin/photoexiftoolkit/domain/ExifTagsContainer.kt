package com.angelgladin.photoexiftoolkit.domain

/**
 * Created on 12/24/16.
 */
data class ExifTagsContainer(val list: List<ExifField>, val type: Type) {
  fun getOnStringProperties(): String {
    var s = StringBuilder()
    list.forEach { s.append("${it.tag}: ${it.attribute}\n") }
    return s.toString().substring(0, s.length - 2)
  }
}

enum class Type { LOCATION_DATA, DATE, CAMERA_PROPERTIES, DIMENSION, OTHER }