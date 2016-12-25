package com.angelgladin.photoexiftoolkit.view

import android.graphics.Bitmap
import android.net.Uri
import com.angelgladin.photoexiftoolkit.common.BaseView
import com.angelgladin.photoexiftoolkit.domain.ExifTagsContainer
import java.util.*

/**
 * Created on 12/22/16.
 */
interface PhotoDetailView : BaseView {
  fun setupUI(bitmap: Bitmap)
  fun setImage(fileName: String, fileSize: String, imageUri: Uri)
  fun setExifDataList(list: List<ExifTagsContainer>)
  fun showDialog(item: ExifTagsContainer)
}