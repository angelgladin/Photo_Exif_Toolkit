package com.angelgladin.photoexiftoolkit.data

import com.angelgladin.photoexiftoolkit.data.domain.AddressResponse
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

/**
 * Created on 12/30/16.
 */
interface GoogleMapsApi {
    @GET(ApiConstants.ADDRESS_URL)
    fun getAddressObservable(@Query(ApiConstants.LATLNG_QUERY) latlng: String): Observable<AddressResponse>
}