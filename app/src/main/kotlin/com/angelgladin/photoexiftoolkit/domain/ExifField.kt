package com.angelgladin.photoexiftoolkit.domain

import java.io.Serializable

/**
 * Created on 12/22/16.
 */
data class ExifField(val tag: String, val attribute: String) : Serializable

data class Location(val latitude: Double, val longitude: Double)