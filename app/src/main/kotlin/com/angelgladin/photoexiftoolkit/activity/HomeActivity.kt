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


package com.angelgladin.photoexiftoolkit.activity

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.angelgladin.photoexiftoolkit.R
import com.angelgladin.photoexiftoolkit.extension.getPathFromUri
import com.angelgladin.photoexiftoolkit.extension.showSnackbar
import com.angelgladin.photoexiftoolkit.presenter.HomePresenter
import com.angelgladin.photoexiftoolkit.util.Constants
import com.angelgladin.photoexiftoolkit.view.HomeView
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.content_home.*

class HomeActivity : AppCompatActivity(), HomeView {

    companion object {
        val PICK_IMAGE_REQUEST = 666
    }

    val presenter = HomePresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)

        setupUiListeners()
    }

    private fun setupUiListeners() {
        card_view_from_gallery.setOnClickListener { requestPermissionsAndOpenGallery() }
    }

    private fun requestPermissionsAndOpenGallery() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted())
                            presenter.getPhotoFromGallery()
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>, token: PermissionToken) {
                        AlertDialog.Builder(this@HomeActivity).setTitle(R.string.app_name)
                                .setMessage(R.string.permission_rationale_message)
                                .setNegativeButton(android.R.string.cancel, { dialog, _ ->
                                    dialog.dismiss()
                                    token.cancelPermissionRequest()
                                })
                                .setPositiveButton(android.R.string.ok, { dialog, _ ->
                                    dialog.dismiss()
                                    token.continuePermissionRequest()
                                })
                                .setOnDismissListener({ token.cancelPermissionRequest() })
                                .show()
                    }
                })
                .check()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.data != null) {
            val selectedImage = data.data
            val pathFile = selectedImage.getPathFromUri(this)

            presenter.launchPhotoDetailActivity(pathFile)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_about_developer -> {
            presenter.aboutDeveloper()
            true
        }
        R.id.action_about_app -> {
            presenter.aboutApp()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun getContext(): Context = this

    override fun destroy() {
    }

    override fun openGallery() {
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, PICK_IMAGE_REQUEST)
    }

    override fun showAboutDeveloperDialog() {
        coordinator_layout.showSnackbar(R.string.not_implemented_yet)
    }

    override fun showAboutAppDialog() {
        coordinator_layout.showSnackbar(R.string.not_implemented_yet)
    }

    override fun launchPhotoDetailActivity(pathFile: String?) {
        val intent = Intent(this, PhotoDetailActivity::class.java)
        intent.putExtra(Constants.PATH_FILE_KEY, pathFile)
        startActivity(intent)
    }

}
