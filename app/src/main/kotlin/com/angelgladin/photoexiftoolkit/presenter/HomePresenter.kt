package com.angelgladin.photoexiftoolkit.presenter

import com.angelgladin.photoexiftoolkit.common.BasePresenter
import com.angelgladin.photoexiftoolkit.common.BaseView
import com.angelgladin.photoexiftoolkit.view.HomeView

/**
 * Created on 12/22/16.
 */
class HomePresenter(override val view: HomeView) : BasePresenter<BaseView> {
    override fun initialize() {
    }

    fun getPhotoFromGallery() = view.openGallery()

    fun aboutDeveloper() = view.showAboutDeveloperDialog()

    fun aboutApp() = view.showAboutAppDialog()

    fun launchPhotoDetailActivity(pathFile: String?) = view.launchPhotoDetailActivity(pathFile)
}