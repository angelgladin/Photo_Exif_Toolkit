package com.angelgladin.photoexiftoolkit.presenter

import android.content.Intent
import android.media.ExifInterface
import android.net.Uri
import android.util.Log
import com.angelgladin.photoexiftoolkit.common.BasePresenter
import com.angelgladin.photoexiftoolkit.common.BaseView
import com.angelgladin.photoexiftoolkit.domain.ExifField
import com.angelgladin.photoexiftoolkit.domain.ExifTagsContainer
import com.angelgladin.photoexiftoolkit.domain.Type
import com.angelgladin.photoexiftoolkit.extension.getMap
import com.angelgladin.photoexiftoolkit.extension.getSize
import com.angelgladin.photoexiftoolkit.util.Constants
import com.angelgladin.photoexiftoolkit.view.PhotoDetailView
import java.io.File

/**
 * Created on 12/22/16.
 */
class PhotoDetailPresenter(override val view: PhotoDetailView) : BasePresenter<BaseView> {

    lateinit var exifTagsContainerList: List<ExifTagsContainer>

    override fun initialize() {
    }

    fun getDataFromIntent(intent: Intent) {
        val filePath = intent.getStringExtra(Constants.PATH_FILE_KEY)

        val exifInterface = ExifInterface(filePath)
        Log.e(this.javaClass.simpleName, filePath)
        exifTagsContainerList = transformList(exifInterface.getMap())

        val imageUri = Uri.fromFile(File(filePath))
        val file = File(filePath)
        view.setImage(file.name, file.getSize(), imageUri)

        view.setExifDataList(exifTagsContainerList)
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
                        || it.key == ExifInterface.TAG_MODEL
                        || it.key == ExifInterface.TAG_F_NUMBER
                        || it.key == ExifInterface.TAG_EXPOSURE_TIME
                        || it.key == ExifInterface.TAG_ISO_SPEED_RATINGS
                        || it.key == ExifInterface.TAG_FLASH ->
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

    fun pressedItem(item: ExifTagsContainer) {
        view.showAlertDialogWhenItemIsPressed(item)
    }

    fun copyDataToClipboard(item: ExifTagsContainer) = view.copyDataToClipboard(item)

    fun editLocation(item: ExifTagsContainer) {

    }

    fun editDate(item: ExifTagsContainer) {

    }

    fun editExifFieldsOpeningDialog(item: ExifTagsContainer) {

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

    fun clearExif() {
        //TODO:
    }

}