package com.angelgladin.photoexiftoolkit.activity

import android.content.Context
import android.content.Intent
import android.media.ExifInterface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.angelgladin.photoexiftoolkit.R
import com.angelgladin.photoexiftoolkit.domain.ExifField
import com.angelgladin.photoexiftoolkit.extension.getMap
import com.angelgladin.photoexiftoolkit.extension.getPathFromUri
import com.angelgladin.photoexiftoolkit.presenter.HomePresenter
import com.angelgladin.photoexiftoolkit.view.HomeView
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.content_home.*
import java.util.*

class HomeActivity : AppCompatActivity(), HomeView {

  val PICK_IMAGE_REQUEST = 666

  val presenter = HomePresenter(this)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_home)
    setSupportActionBar(toolbar)
    setupOnClickListeners()
  }

  private fun setupOnClickListeners() {
    button_from_gallery.setOnClickListener { presenter.getPhotoFromGallery() }
    button_from_url.setOnClickListener { presenter.getPhotoFromUrl() }
    button_file_picker.setOnClickListener { presenter.getPhotoFromFilePicker() }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
        && data != null && data.data != null) {
      val selectedImage = data.data

      val pathFile = selectedImage.getPathFromUri(this)
      val exifInterface = ExifInterface(pathFile)

      presenter.launchPhotoDetailActivity(pathFile!!, exifInterface.getMap())
    }
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.menu_main, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.action_about_developer -> presenter.aboutDeveloper()
      R.id.action_about_app -> presenter.aboutApp()
    }
    return super.onOptionsItemSelected(item)
  }

  override fun getContext(): Context = this

  override fun destroy() {
  }

  override fun openGallery() {
    val photoPickerIntent = Intent(Intent.ACTION_PICK)
    photoPickerIntent.type = "image/*"
    startActivityForResult(photoPickerIntent, PICK_IMAGE_REQUEST)
  }

  override fun openUrlDialog() {
  }

  override fun openFilePicker() {
  }

  override fun showAboutDeveloperDialog() {
  }

  override fun showAboutAppDialog() {
  }

  override fun launchPhotoDetailActivity(pathFile: String, list: ArrayList<ExifField>,
      availableLocation: Boolean) {
    val intent = Intent(this, PhotoDetailActivity::class.java)
    intent.putExtra("path_file", pathFile)
    intent.putExtra("list", list)
    intent.putExtra("available_location", availableLocation)
    startActivity(intent)
  }

  override fun showOnErrorDialog() {
  }
}
