package com.angelgladin.photoexiftoolkit.dialog

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.angelgladin.photoexiftoolkit.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

/**
 * Created on 12/22/16.
 */
class MapDialog : DialogFragment(), OnMapReadyCallback {
  var mMap: GoogleMap? = null

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    return inflater.inflate(R.layout.dialog_maps, container, false)
  }

  override fun onResume() {
    super.onResume()
    val mapFragment = fragmentManager.findFragmentById(R.id.map) as SupportMapFragment
    mapFragment.getMapAsync(this)
  }

  override fun onDestroyView() {
    super.onDestroyView()
    val f = fragmentManager.findFragmentById(R.id.map) as SupportMapFragment
    if (f != null) fragmentManager.beginTransaction().remove(f).commit()
  }

  override fun onMapReady(googleMap: GoogleMap) {
    mMap = googleMap

    val sydney = LatLng(19.422222, -99.16)

    mMap!!.isMyLocationEnabled = true
    mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 14.8f))

    mMap!!.addMarker(MarkerOptions()
        .title("Sydney")
        .snippet("The most populous city in Australia.")
        .position(sydney))
  }

}