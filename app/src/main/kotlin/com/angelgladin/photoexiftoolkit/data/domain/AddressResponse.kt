package com.angelgladin.photoexiftoolkit.data.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created on 12/30/16.
 */
data class AddressResponse(@SerializedName("status") @Expose val status: String,
                           @SerializedName("results") @Expose val resultList: List<Result>)

data class Result(@SerializedName("formatted_address") @Expose val formattedAddress: String)
