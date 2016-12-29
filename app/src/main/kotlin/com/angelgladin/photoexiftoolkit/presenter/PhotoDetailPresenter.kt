package com.angelgladin.photoexiftoolkit.presenter

import android.content.Intent
import android.media.ExifInterface
import android.net.Uri
import android.util.Log
import com.angelgladin.photoexiftoolkit.common.BasePresenter
import com.angelgladin.photoexiftoolkit.common.BaseView
import com.angelgladin.photoexiftoolkit.domain.ExifField
import com.angelgladin.photoexiftoolkit.domain.ExifTagsContainer
import com.angelgladin.photoexiftoolkit.domain.Location
import com.angelgladin.photoexiftoolkit.domain.Type
import com.angelgladin.photoexiftoolkit.extension.*
import com.angelgladin.photoexiftoolkit.util.Constants
import com.angelgladin.photoexiftoolkit.view.PhotoDetailView
import java.io.File
import java.io.IOException

/**
 * Created on 12/22/16.
 */
class PhotoDetailPresenter(override val view: PhotoDetailView) : BasePresenter<BaseView> {

    lateinit var exifTagsContainerList: List<ExifTagsContainer>
    lateinit var exifInterface: ExifInterface
    lateinit var filePath: String

    override fun initialize() {
    }

    fun getDataFromIntent(intent: Intent) {
        filePath = intent.getStringExtra(Constants.PATH_FILE_KEY)
        Log.e(this.javaClass.simpleName, filePath)

        updateExifTagsContainerList()

        val imageUri = Uri.fromFile(File(filePath))
        val file = File(filePath)
        view.setImage(file.name, file.getSize(), imageUri)

        view.setExifDataList(exifTagsContainerList)
    }

    private fun updateExifTagsContainerList() {
        exifInterface = ExifInterface(filePath)
        exifTagsContainerList = transformList(exifInterface.getMap())
    }

    private fun transformList(map: MutableMap<String, String>): List<ExifTagsContainer> {
        val locationsList = arrayListOf<ExifField>()
        val datesList = arrayListOf<ExifField>()
        val cameraPropertiesList = arrayListOf<ExifField>()
        val dimensionsList = arrayListOf<ExifField>()
        val othersList = arrayListOf<ExifField>()

        map.forEach {
            when {
                it.key == Constants.EXIF_LATITUDE
                        || it.key == Constants.EXIF_LONGITUDE
                        || it.key == ExifInterface.TAG_GPS_LATITUDE
                        || it.key == ExifInterface.TAG_GPS_LATITUDE_REF
                        || it.key == ExifInterface.TAG_GPS_LONGITUDE
                        || it.key == ExifInterface.TAG_GPS_LONGITUDE_REF ->
                    locationsList.add(ExifField(it.key, it.value))

                it.key == ExifInterface.TAG_DATETIME
                        || it.key == ExifInterface.TAG_GPS_DATESTAMP
                        || it.key == ExifInterface.TAG_DATETIME_DIGITIZED ->
                    datesList.add(ExifField(it.key, it.value))

                it.key == ExifInterface.TAG_MAKE
                        || it.key == ExifInterface.TAG_MODEL ->
                    cameraPropertiesList.add(ExifField(it.key, it.value))

                it.key == ExifInterface.TAG_IMAGE_LENGTH
                        || it.key == ExifInterface.TAG_IMAGE_WIDTH ->
                    dimensionsList.add(ExifField(it.key, it.value))

                else -> othersList.add(ExifField(it.key, it.value))
            }
        }
        return arrayListOf(ExifTagsContainer(locationsList, Type.LOCATION_DATA),
                ExifTagsContainer(datesList, Type.DATE),
                ExifTagsContainer(cameraPropertiesList, Type.CAMERA_PROPERTIES),
                ExifTagsContainer(dimensionsList, Type.DIMENSION),
                ExifTagsContainer(othersList, Type.OTHER))
    }

    fun onItemPressed(item: ExifTagsContainer) {
        view.showAlertDialogWhenItemIsPressed(item)
    }

    fun copyDataToClipboard(item: ExifTagsContainer) = view.copyDataToClipboard(item)


    fun editDate(item: ExifTagsContainer) {

    }

    fun openDialogMap(item: ExifTagsContainer) {
        val latitude = item.list.find { it.tag == Constants.EXIF_LATITUDE }?.attribute?.toDouble()
        val longitude = item.list.find { it.tag == Constants.EXIF_LONGITUDE }?.attribute?.toDouble()
        view.openDialogMap(latitude, longitude)
    }

    fun shareData() {
        val s = exifTagsContainerList
                .map { "\n\n${it.type.name}:\n${it.getOnStringProperties()}" }.toString()
        view.shareData(s.substring(3, s.length - 1))
    }


    fun changeLocation(location: Location) {
        try {
            exifInterface.apply {
                setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, exifInterface.getLatitudeRef(location.latitude))
                setAttribute(ExifInterface.TAG_GPS_LATITUDE, exifInterface.convertDecimalToDegrees(location.latitude))
                setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, exifInterface.getLongitudeRef(location.longitude))
                setAttribute(ExifInterface.TAG_GPS_LONGITUDE, exifInterface.convertDecimalToDegrees(location.longitude))
            }
            exifInterface.saveAttributes()

            updateExifTagsContainerList()
            view.changeExifDataList(exifTagsContainerList)

            view.onCompleteLocationChanged()
        } catch (e: IOException) {
            view.onError("Cannot change location data", e)
        }
    }


}