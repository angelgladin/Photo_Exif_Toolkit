package com.angelgladin.photoexiftoolkit.presenter

import android.content.Intent
import android.graphics.BitmapFactory
import com.angelgladin.photoexiftoolkit.common.BasePresenter
import com.angelgladin.photoexiftoolkit.common.BaseView
import com.angelgladin.photoexiftoolkit.domain.ExifField
import com.angelgladin.photoexiftoolkit.extension.getSize
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

    val bitmap = BitmapFactory.decodeFile(filePath)
    view.setupUI(bitmap)

    val file = File(filePath)
    view.setImageData(file.name, file.getSize())
    view.setExifFieldsList(list)
  }

}