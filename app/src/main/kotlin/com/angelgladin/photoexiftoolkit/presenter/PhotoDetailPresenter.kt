/**
 * Photo EXIF Toolkit for Android.
 *
 * Copyright (C) 2017 Ángel Iván Gladín García
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


package com.angelgladin.photoexiftoolkit.presenter

import android.content.Intent
import android.media.ExifInterface
import android.net.Uri
import android.util.Log
import com.angelgladin.photoexiftoolkit.R
import com.angelgladin.photoexiftoolkit.common.BasePresenter
import com.angelgladin.photoexiftoolkit.common.BaseView
import com.angelgladin.photoexiftoolkit.domain.ExifField
import com.angelgladin.photoexiftoolkit.domain.ExifTagsContainer
import com.angelgladin.photoexiftoolkit.domain.Location
import com.angelgladin.photoexiftoolkit.domain.Type
import com.angelgladin.photoexiftoolkit.extension.*
import com.angelgladin.photoexiftoolkit.interactor.PhotoDetailInteractor
import com.angelgladin.photoexiftoolkit.util.Constants
import com.angelgladin.photoexiftoolkit.view.PhotoDetailView
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created on 12/22/16.
 */
class PhotoDetailPresenter(override val view: PhotoDetailView) : BasePresenter<BaseView> {

    lateinit var exifTagsContainerList: List<ExifTagsContainer>
    lateinit var exifInterface: ExifInterface
    lateinit var filePath: String

    var latitude: Double? = null
    var longitude: Double? = null

    override fun initialize() {
    }

    fun initialize(intent: Intent) {
        filePath = intent.getStringExtra(Constants.PATH_FILE_KEY)
        computeTags()
        setImageByGivenAPath()
        populateExifProperties()
        getAddressByTriggerRequest()
    }

    private fun computeTags() {
        exifInterface = ExifInterface(filePath)
        val map = exifInterface.getTags()
        exifTagsContainerList = transformList(map)

        latitude = map[Constants.EXIF_LATITUDE]?.toDouble()
        longitude = map[Constants.EXIF_LONGITUDE]?.toDouble()
    }

    private fun populateExifProperties() {
        view.setExifDataList(exifTagsContainerList)
    }

    private fun setImageByGivenAPath() {
        Log.d(this.javaClass.simpleName, filePath)
        val imageUri = Uri.fromFile(File(filePath))
        val file = File(filePath)
        view.setImage(file.name, file.getSize(), imageUri)
    }

    private fun getAddressByTriggerRequest() {
        if (latitude != null && longitude != null) {
            view.showProgressDialog()
            PhotoDetailInteractor.getAddress(latitude!!, longitude!!,
                    onResponse = {
                        Log.d(this.javaClass.simpleName, it)
                        view.showAddressOnRecyclerViewItem(it)
                        view.hideProgressDialog()
                    },
                    onFailure = {
                        Log.e(this.javaClass.simpleName, it.message)
                        view.onError(view.getContext().resources.getString(R.string.getting_address_error))
                        view.hideProgressDialog()
                    })

        }
    }

    private fun transformList(map: MutableMap<String, String>): List<ExifTagsContainer> {
        val locationsList = arrayListOf<ExifField>()
        val gpsList = arrayListOf<ExifField>()
        val datesList = arrayListOf<ExifField>()
        val cameraPropertiesList = arrayListOf<ExifField>()
        val dimensionsList = arrayListOf<ExifField>()
        val othersList = arrayListOf<ExifField>()
        map.forEach {
            when (it.key) {
                Constants.EXIF_LATITUDE
                    , Constants.EXIF_LONGITUDE ->
                    locationsList.add(ExifField(it.key, it.value))
                ExifInterface.TAG_DATETIME
                    , ExifInterface.TAG_DATETIME_DIGITIZED ->
                    datesList.add(ExifField(it.key, it.value))
                ExifInterface.TAG_MAKE
                    , ExifInterface.TAG_MODEL ->
                    cameraPropertiesList.add(ExifField(it.key, it.value))
                ExifInterface.TAG_IMAGE_LENGTH
                    , ExifInterface.TAG_IMAGE_WIDTH ->
                    dimensionsList.add(ExifField(it.key, it.value))
                else -> {
                    if (it.key.contains("GPS")) gpsList.add(ExifField(it.key, it.value))
                    else othersList.add(ExifField(it.key, it.value))
                }
            }
        }
        locationsList.addAll(gpsList)
        return arrayListOf(ExifTagsContainer(locationsList, Type.GPS),
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
        val year: Int
        val month: Int
        val day: Int
        if (item.list.isEmpty()) {
            val calendar = Calendar.getInstance()
            year = calendar.get(Calendar.YEAR)
            month = calendar.get(Calendar.MONTH)
            day = calendar.get(Calendar.DAY_OF_MONTH)
        } else {
            val date = item.list.first().attribute
            year = date.substring(0, 4).toInt()
            month = date.substring(5, 7).toInt() - 1
            day = date.substring(8, 10).toInt()
        }
        view.showDialogEditDate(year, month, day)
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

    //TODO: refactor and clean code.
    fun changeExifLocation(location: Location) {
        try {
            exifInterface.apply {
                setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, exifInterface.getLatitudeRef(location.latitude))
                setAttribute(ExifInterface.TAG_GPS_LATITUDE, exifInterface.convertDecimalToDegrees(location.latitude))
                setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, exifInterface.getLongitudeRef(location.longitude))
                setAttribute(ExifInterface.TAG_GPS_LONGITUDE, exifInterface.convertDecimalToDegrees(location.longitude))
            }
            exifInterface.saveAttributes()

            computeTags()
            view.changeExifDataList(exifTagsContainerList)

            getAddressByTriggerRequest()
            view.onCompleteLocationChanged()
        } catch (e: IOException) {
            view.onError(view.getContext().resources.getString(R.string.location_changed_message_error))
        }
    }

    //TODO: refactor and clean code.
    fun changeExifDate(year: Int, month: Int, dayOfMonth: Int) {
        val locationExifContainerList = exifTagsContainerList.find { it.type == Type.DATE }?.list!!
        val dateTimeShort: String
        val dateTimeLong: String

        if (locationExifContainerList.isEmpty()) {
            val df = SimpleDateFormat("HH:mm:ss")
            val calendar = Calendar.getInstance()
            dateTimeLong = "$year:${appendZeroIfNeeded(month)}:${appendZeroIfNeeded(dayOfMonth)} ${df.format(calendar.time)}"
            dateTimeShort = ""
        } else {
            val auxListLong = mutableListOf<ExifField>()
            locationExifContainerList.forEach {
                if (it.attribute.length > 10) auxListLong.add(it)
            }
            val actualDate = auxListLong.first().attribute.substring(11)
            dateTimeLong = "$year:${appendZeroIfNeeded(month)}:${appendZeroIfNeeded(dayOfMonth)} $actualDate"
            dateTimeShort = "$year:${appendZeroIfNeeded(month)}:${appendZeroIfNeeded(dayOfMonth)}"
        }
        try {
            if (locationExifContainerList.isEmpty()) {
                exifInterface.setAttribute(ExifInterface.TAG_DATETIME, dateTimeLong)
            } else {
                locationExifContainerList.forEach {
                    if (it.attribute.length > 10)
                        exifInterface.setAttribute(it.tag, dateTimeLong)
                    else
                        exifInterface.setAttribute(it.tag, dateTimeShort)
                }
            }
            exifInterface.saveAttributes()

            computeTags()
            view.changeExifDataList(exifTagsContainerList)

            view.onCompleteDateChanged()
            Log.d(this.javaClass.simpleName, "Date was changed: year: $year  month: $month day: $dayOfMonth")
        } catch (e: IOException) {
            Log.e(this.javaClass.simpleName, "${e.cause} - ${e.message}")
            view.onError(view.getContext().resources.getString(R.string.date_changed_message_error))
        }
    }

    private fun appendZeroIfNeeded(n: Int): String {
        val s = n.toString()
        if (s.length == 1)
            return "0$s"
        else
            return s
    }

    fun removeAllTags() {
        exifInterface.removeAllTags(
                onSuccess = {
                    refreshTags()
                    view.hideAddressOnRecyclerViewItem()
                    view.onSuccessTagsDeleted(view.getContext().getString(R.string.all_tags_deleted_successfully))
                },
                onFailure = { view.onError(view.getContext().getString(R.string.something_went_wrong)) })
    }

    fun removeTags(tags: Set<String>) {
        if (tags.isNotEmpty())
            exifInterface.removeTags(tags,
                    onSuccess = {
                        refreshTags()
                        view.hideAddressOnRecyclerViewItem()
                        view.onSuccessTagsDeleted(view.getContext().getString(R.string.tags_deleted_successfully))
                    },
                    onFailure = { view.onError(view.getContext().getString(R.string.something_went_wrong)) })
    }

    private fun refreshTags() {
        computeTags()
        view.changeExifDataList(exifTagsContainerList)
    }

}