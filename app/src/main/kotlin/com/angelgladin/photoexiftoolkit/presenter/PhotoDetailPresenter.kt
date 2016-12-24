package com.angelgladin.photoexiftoolkit.presenter

import android.content.Intent
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.net.Uri
import android.util.Log
import com.angelgladin.photoexiftoolkit.common.BasePresenter
import com.angelgladin.photoexiftoolkit.common.BaseView
import com.angelgladin.photoexiftoolkit.domain.ExifField
import com.angelgladin.photoexiftoolkit.extension.getSize
import com.angelgladin.photoexiftoolkit.util.Constants
import com.angelgladin.photoexiftoolkit.view.PhotoDetailView
import java.io.File
import java.util.*

/**
 * Created on 12/22/16.
 */
class PhotoDetailPresenter(override val view: PhotoDetailView) : BasePresenter<BaseView> {

  override fun initialize() {
  }

  fun getDataFromIntent(intent: Intent) {
    val filePath = intent.getStringExtra("path_file")
    val list = intent.getSerializableExtra("list") as ArrayList<ExifField>
    //Log.e("PATH", filePath)

    test(list)

    val bitmap = BitmapFactory.decodeFile(filePath)
    view.setupUI(bitmap)

    val imageUri = Uri.fromFile(File(filePath))
    val file = File(filePath)
    view.setImage(file.name, file.getSize(), imageUri)
    view.setExifFieldsList(list)
  }

  private fun test(list: ArrayList<ExifField>) {
    val locationsList = arrayListOf<ExifField>()
    val datesList = arrayListOf<ExifField>()
    val cameraPropertiesList = arrayListOf<ExifField>()
    val dimensionsList = arrayListOf<ExifField>()
    val othersList = arrayListOf<ExifField>()

    list.forEach {
      when {
        it.tag == Constants.EXIF_LATITUDE -> locationsList.add(it)
        it.tag == Constants.EXIF_LONGITUDE -> locationsList.add(it)
        it.tag == ExifInterface.TAG_GPS_LATITUDE -> locationsList.add(it)
        it.tag == ExifInterface.TAG_GPS_LATITUDE_REF -> locationsList.add(it)
        it.tag == ExifInterface.TAG_GPS_LATITUDE -> locationsList.add(it)
        it.tag == ExifInterface.TAG_GPS_LONGITUDE_REF -> locationsList.add(it)

        it.tag == ExifInterface.TAG_DATETIME -> datesList.add(it)
        it.tag == ExifInterface.TAG_GPS_DATESTAMP -> datesList.add(it)
        it.tag == ExifInterface.TAG_DATETIME_DIGITIZED -> datesList.add(it)

        it.tag == ExifInterface.TAG_MAKE -> cameraPropertiesList.add(it)
        it.tag == ExifInterface.TAG_MODEL -> cameraPropertiesList.add(it)
        it.tag == ExifInterface.TAG_F_NUMBER -> cameraPropertiesList.add(it)
        it.tag == ExifInterface.TAG_EXPOSURE_TIME -> cameraPropertiesList.add(it)
        it.tag == ExifInterface.TAG_ISO_SPEED_RATINGS -> cameraPropertiesList.add(it)
        it.tag == ExifInterface.TAG_FLASH -> cameraPropertiesList.add(it)

        it.tag == ExifInterface.TAG_IMAGE_LENGTH -> dimensionsList.add(it)
        it.tag == ExifInterface.TAG_IMAGE_WIDTH -> dimensionsList.add(it)

        else -> othersList.add(it)
      }
    }
  }

}