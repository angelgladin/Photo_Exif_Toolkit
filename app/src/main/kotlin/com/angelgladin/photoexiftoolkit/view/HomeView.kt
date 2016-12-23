package com.angelgladin.photoexiftoolkit.view

import com.angelgladin.photoexiftoolkit.common.BaseView
import com.angelgladin.photoexiftoolkit.domain.ExifField
import java.util.*

/**
 * Created on 12/22/16.
 */
interface HomeView : BaseView {
  fun openGallery()
  fun openUrlDialog()
  fun openFilePicker()
  fun showAboutDeveloperDialog()
  fun showAboutAppDialog()
  fun launchPhotoDetailActivity(list: ArrayList<ExifField>, availableLocation: Boolean)
  fun showOnErrorDialog()
}