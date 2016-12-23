package com.angelgladin.photoexiftoolkit.activity

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.graphics.Palette
import android.util.Log
import android.view.Menu
import com.angelgladin.photoexiftoolkit.R
import com.angelgladin.photoexiftoolkit.domain.ExifField
import com.angelgladin.photoexiftoolkit.presenter.PhotoDetailPresenter
import com.angelgladin.photoexiftoolkit.view.PhotoDetailView
import kotlinx.android.synthetic.main.activity_photo_detail.*
import java.util.*

class PhotoDetailActivity : AppCompatActivity(), PhotoDetailView {

  val presenter = PhotoDetailPresenter(this)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_photo_detail)
    setSupportActionBar(toolbar)

    presenter.getDataFromIntent(intent)
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_photo_detail, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun getContext(): Context = this

  override fun destroy() {
  }

  override fun setImageData(fileName: String, fileSize: String) {
    Log.e("AAAAA", "file name: " + fileName)
    Log.e("AAAAA", "file size: " + fileSize)
  }

  override fun setExifFieldsList(list: ArrayList<ExifField>) {
    Log.e("AAAAA", list.toString())
  }

  override fun setupUI(bitmap: Bitmap) {
    toolbar_layout.setExpandedTitleColor(resources.getColor(android.R.color.transparent))

    image_photo.setImageBitmap(bitmap)
    val palette = Palette.from(bitmap).generate()
    val vibrant = palette.dominantSwatch;
    if (vibrant != null) {
      toolbar_layout.setContentScrimColor(vibrant.rgb)
    }
  }
}
