package com.angelgladin.photoexiftoolkit.domain

/**
 * Created on 12/24/16.
 */
data class ExifTagsContainer(val list: List<ExifField>, val type: Type)

enum class Type { LOCATION_DATA, DATE, CAMERA_PROPERTIES, DIMENSION, OTHER }