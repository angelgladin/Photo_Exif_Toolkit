package com.angelgladin.photoexiftoolkit.data

import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created on 12/30/16.
 */
object GoogleMapsService {
    val googleMapsApi: GoogleMapsApi

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl(ApiConstants.BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        googleMapsApi = retrofit.create(GoogleMapsApi::class.java)
    }
}