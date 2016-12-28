package com.angelgladin.photoexiftoolkit.view

import android.net.Uri
import com.angelgladin.photoexiftoolkit.common.BaseView
import com.angelgladin.photoexiftoolkit.domain.ExifTagsContainer

/**
 * Created on 12/22/16.
 */
interface PhotoDetailView : BaseView {
    fun setImage(fileName: String, fileSize: String, imageUri: Uri)
    fun setExifDataList(list: List<ExifTagsContainer>)
    fun showAlertDialogWhenItemIsPressed(item: ExifTagsContainer)
    fun copyDataToClipboard(item: ExifTagsContainer)
    fun editLocation(item: ExifTagsContainer)
    fun editDate(item: ExifTagsContainer)
    fun editExifFieldsOpeningDialog(item: ExifTagsContainer)
    fun openDialogMap(latitude: Double?, longitude: Double?)
    fun shareData(data: String)
    fun clearData(list: List<ExifTagsContainer>)
    fun onError()
}