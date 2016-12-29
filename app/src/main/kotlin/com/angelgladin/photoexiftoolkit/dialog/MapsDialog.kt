package com.angelgladin.photoexiftoolkit.dialog

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.*
import com.angelgladin.photoexiftoolkit.R
import com.angelgladin.photoexiftoolkit.domain.Location
import com.angelgladin.photoexiftoolkit.util.Constants
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

/**
 * Created on 12/22/16.
 */
class MapDialog : DialogFragment(), OnMapReadyCallback, GoogleMap.OnMapClickListener, Toolbar.OnMenuItemClickListener {

    lateinit var dialogEvents: DialogEvents

    lateinit var toolbar: Toolbar

    lateinit var mMap: GoogleMap
    lateinit var location: LatLng
    lateinit var marker: Marker

    var editModeLocation = false
    var locationChanged = false

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

        toolbar = view.findViewById(R.id.toolbar) as Toolbar
        toolbar.apply {
            inflateMenu(R.menu.menu_dialog_maps)
            setOnMenuItemClickListener(this@MapDialog)
            setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
            setNavigationOnClickListener { dismiss() }
            title = resources.getString(R.string.dialog_maps_title)
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        val mapFragment = fragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dialogEvents.locationChanged(locationChanged, Location(location.latitude, location.longitude))

        val f = fragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        if (f != null) fragmentManager.beginTransaction().remove(f).commit()
    }

    override fun onMenuItemClick(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_edit_location -> {
            editLocation()
            true
        }
        R.id.action_done_editing -> {
            doneEditing()
            true
        }
        else -> false
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        location = LatLng(arguments.getDouble(Constants.EXIF_LATITUDE), arguments.getDouble(Constants.EXIF_LONGITUDE))

        mMap.apply {
            setOnMapClickListener(this@MapDialog)
            uiSettings.isZoomControlsEnabled = true
            isMyLocationEnabled = true
            moveCamera(CameraUpdateFactory.newLatLngZoom(location, 14.8f))
        }
        addMarker(location)
    }

    override fun onMapClick(latLng: LatLng) {
        if (editModeLocation) {
            Log.e(this.javaClass.simpleName, "Latitude ${latLng.latitude} -- Longitude ${latLng.longitude}")
            marker.remove()
            addMarker(latLng)
        }
    }

    private fun editLocation() {
        editModeLocation = true
        toolbar.title = "Tap for new location"
        toolbar.menu.findItem(R.id.action_edit_location).isVisible = false
        toolbar.menu.findItem(R.id.action_done_editing).isVisible = true
    }

    private fun doneEditing() {
        editModeLocation = false
        AlertDialog.Builder(context)
                .setTitle("hola")
                .setMessage(resources.getString(R.string.lorem_ipsum))
                .setPositiveButton(resources.getString(android.R.string.ok),
                        { dialogInterface, i -> locationChanged = true })
                .setNegativeButton(resources.getString(android.R.string.cancel),
                        { dialogInterface, i ->
                            marker.remove()
                            addMarker(location)
                        })
                .show()

        toolbar.title = resources.getString(R.string.dialog_maps_title)
        toolbar.menu.findItem(R.id.action_done_editing).isVisible = false
        toolbar.menu.findItem(R.id.action_edit_location).isVisible = true
    }

    private fun addMarker(location: LatLng) {
        marker = mMap.addMarker(MarkerOptions().position(location))
        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        try {
            dialogEvents = (activity as? DialogEvents)!!
        } catch (e: ClassCastException) {
            throw ClassCastException(activity.toString() + " must implement DialogEvents")
        }
    }

    interface DialogEvents {
        fun locationChanged(locationChanged: Boolean, location: Location)
    }
}