package com.angelgladin.photoexiftoolkit.view

import com.angelgladin.photoexiftoolkit.common.BaseView

/**
 * Created on 12/22/16.
 */
interface HomeView : BaseView {
    fun openGallery()
    fun openUrlDialog()
    fun openFilePicker()
    fun showAboutDeveloperDialog()
    fun showAboutAppDialog()
    fun launchPhotoDetailActivity(pathFile: String?)
    fun showOnErrorDialog()
}