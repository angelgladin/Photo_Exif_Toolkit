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
    fun showAddressOnRecyclerViewItem(address: String)
    fun showProgressDialog()
    fun hideProgressDialog()
    fun changeExifDataList(list: List<ExifTagsContainer>)
    fun showAlertDialogWhenItemIsPressed(item: ExifTagsContainer)
    fun copyDataToClipboard(item: ExifTagsContainer)
    fun openDialogMap(latitude: Double?, longitude: Double?)
    fun showDialogEditDate(year: Int, month: Int, day: Int)
    fun shareData(data: String)
    fun onCompleteLocationChanged()
    fun onCompleteDateChanged()
    fun onError(message: String, t: Throwable)
}