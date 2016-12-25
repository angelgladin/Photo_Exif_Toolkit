package com.angelgladin.photoexiftoolkit.activity

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.graphics.Palette
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.WindowManager
import android.widget.Toast
import com.angelgladin.photoexiftoolkit.R
import com.angelgladin.photoexiftoolkit.adapter.ExifFieldsAdapter
import com.angelgladin.photoexiftoolkit.domain.ExifTagsContainer
import com.angelgladin.photoexiftoolkit.domain.Type
import com.angelgladin.photoexiftoolkit.presenter.PhotoDetailPresenter
import com.angelgladin.photoexiftoolkit.view.PhotoDetailView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_photo_detail.*
import kotlinx.android.synthetic.main.content_photo_detail.*

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

  override fun setImage(fileName: String, fileSize: String, imageUri: Uri) {
    text_image_info.text = "$fileName\n$fileSize"
    Glide.with(this).load(imageUri).into(image_photo)
  }

  override fun setExifDataList(
      list: List<ExifTagsContainer>) {
    Log.e("AAAAA", list.toString())

    recycler_view.setHasFixedSize(true)
    val mLayoutManager = LinearLayoutManager(this)
    recycler_view.layoutManager = mLayoutManager
    val mAdapter = ExifFieldsAdapter(list, presenter)
    recycler_view.adapter = mAdapter
  }

  override fun setupUI(bitmap: Bitmap) {
    toolbar_layout.setExpandedTitleColor(resources.getColor(android.R.color.transparent))

    image_photo.setImageBitmap(bitmap)

    val palette: Palette? = Palette.from(bitmap).generate()
    val vibrantPalette = palette?.vibrantSwatch
    val darkVibrantPalette = palette?.darkVibrantSwatch

    if (vibrantPalette != null) {
      toolbar_layout.setContentScrimColor(vibrantPalette.rgb)
    }

    if (darkVibrantPalette != null) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = darkVibrantPalette.rgb
      }
    }
  }

  override fun showDialog(item: ExifTagsContainer) {
    val alertDialogBuilder = AlertDialog.Builder(this)
    val optionsList = mutableListOf("Copy to clipboard", "Edit")
    when (item.type) {
      Type.LOCATION_DATA -> optionsList.add("Open map")
      Type.DIMENSION -> optionsList.remove("Edit")
    }
    alertDialogBuilder.setTitle("Select an action")
    alertDialogBuilder.setItems(optionsList.toTypedArray(), { dialog, which ->
      Toast.makeText(this@PhotoDetailActivity, item.toString(), Toast.LENGTH_SHORT).show()
    })
    val dialog = alertDialogBuilder.create()
    dialog.show()
  }
}
