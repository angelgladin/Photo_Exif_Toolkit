package com.angelgladin.photoexiftoolkit.dialog

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.Toast
import com.angelgladin.photoexiftoolkit.R
import com.angelgladin.photoexiftoolkit.util.Constants
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

/**
 * Created on 12/22/16.
 */
class MapDialog : DialogFragment(), OnMapReadyCallback, Toolbar.OnMenuItemClickListener {

    var mMap: GoogleMap? = null

    companion object {
        fun newInstance(latitude: Double, longitude: Double): MapDialog {
            val frag = MapDialog()
            val bundle = Bundle()
            bundle.putDouble(Constants.EXIF_LATITUDE, latitude)
            bundle.putDouble(Constants.EXIF_LONGITUDE, longitude)
            frag.arguments = bundle
            return frag
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_maps, container, false)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val toolbar = view.findViewById(R.id.toolbar) as Toolbar
        toolbar.inflateMenu(R.menu.menu_dialog_maps)
        toolbar.setOnMenuItemClickListener(this)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        toolbar.setNavigationOnClickListener { dismiss() }
        toolbar.title = resources.getString(R.string.dialog_maps_title)

        return view
    }

    override fun onResume() {
        super.onResume()
        val mapFragment = fragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val f = fragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        if (f != null) fragmentManager.beginTransaction().remove(f).commit()
    }

    override fun onMenuItemClick(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_edit_location -> {
            editLocation()
            true
        }
        else -> false
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap!!.uiSettings.isZoomControlsEnabled = true

        val location = LatLng(arguments.getDouble(Constants.EXIF_LATITUDE),
                arguments.getDouble(Constants.EXIF_LONGITUDE))

        mMap!!.isMyLocationEnabled = true
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 14.8f))

        mMap!!.addMarker(MarkerOptions()
                .title("TODO")
                .snippet("TODO")
                .position(location))
    }

    private fun editLocation() {
        Toast.makeText(context, resources.getString(R.string.not_implemented_yet), Toast.LENGTH_SHORT).show()
    }

}