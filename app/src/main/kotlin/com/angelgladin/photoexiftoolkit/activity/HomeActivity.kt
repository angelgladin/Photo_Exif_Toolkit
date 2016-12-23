package com.angelgladin.photoexiftoolkit.activity

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.angelgladin.photoexiftoolkit.R
import com.angelgladin.photoexiftoolkit.presenter.HomePresenter
import com.angelgladin.photoexiftoolkit.view.HomeView
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.content_home.*

class HomeActivity : AppCompatActivity(), HomeView {

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
  }

  override fun openUrlDialog() {
  }

  override fun openFilePicker() {
  }

  override fun showAboutDeveloperDialog() {
  }

  override fun showAboutAppDialog() {
  }

  override fun showOnErrorDialog() {
  }
}
