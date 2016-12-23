package com.angelgladin.photoexiftoolkit.presenter

import com.angelgladin.photoexiftoolkit.common.BasePresenter
import com.angelgladin.photoexiftoolkit.common.BaseView
import com.angelgladin.photoexiftoolkit.domain.ExifField
import com.angelgladin.photoexiftoolkit.view.HomeView
import java.util.*

/**
 * Created on 12/22/16.
 */
class HomePresenter(override val view: HomeView) : BasePresenter<BaseView> {
  override fun initialize() {

  }

  fun getPhotoFromGallery() = view.openGallery()
  fun getPhotoFromUrl() = view.openUrlDialog()
  fun aboutDeveloper() {
  }

  fun aboutApp() {
  }

  fun getPhotoFromFilePicker() {
  }

  fun launchPhotoDetailActivity(pathFile: String, map: MutableMap<String, String>?) {
    val list: ArrayList<ExifField> = ArrayList()

    var lat: ExifField? = null
    var lon: ExifField? = null
    if (map != null) {
      map.entries.forEach {
        if (it.key == "Latitude") lat = ExifField(it.key, it.value)
        else if (it.key == "Longitude") lon = ExifField(it.key, it.value)
        else list.add(ExifField(it.key, it.value))
      }
      if (map.containsKey("Latitude") && map.containsKey("Longitude")) {
        list.add(0, lon!!)
        list.add(0, lat!!)
      }
    }

    view.launchPhotoDetailActivity(pathFile, list, (lat != null && lon != null))
  }

}