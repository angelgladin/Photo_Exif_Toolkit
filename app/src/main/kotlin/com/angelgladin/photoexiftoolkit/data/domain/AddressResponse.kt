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


package com.angelgladin.photoexiftoolkit.data.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created on 12/30/16.
 */
data class AddressResponse(@SerializedName("status") @Expose val status: String,
                           @SerializedName("results") @Expose val resultList: List<Result>)

data class Result(@SerializedName("formatted_address") @Expose val formattedAddress: String)
