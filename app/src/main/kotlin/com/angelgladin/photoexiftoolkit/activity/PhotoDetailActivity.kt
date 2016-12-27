package com.angelgladin.photoexiftoolkit.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.angelgladin.photoexiftoolkit.R
import com.angelgladin.photoexiftoolkit.adapter.ExifFieldsAdapter
import com.angelgladin.photoexiftoolkit.dialog.MapDialog
import com.angelgladin.photoexiftoolkit.domain.ExifTagsContainer
import com.angelgladin.photoexiftoolkit.domain.Type
import com.angelgladin.photoexiftoolkit.extension.showSnackbar
import com.angelgladin.photoexiftoolkit.presenter.PhotoDetailPresenter
import com.angelgladin.photoexiftoolkit.util.Constants
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
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        presenter.getDataFromIntent(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_photo_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> {
            finish()
            true
        }
        R.id.action_share -> {
            coordinator_layout.showSnackbar(R.string.not_implemented_yet)
            true
        }
        R.id.action_clean_exif -> {
            coordinator_layout.showSnackbar(R.string.not_implemented_yet)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun getContext(): Context = this

    override fun destroy() {
    }

    override fun setImage(fileName: String, fileSize: String, imageUri: Uri) {
        text_image_info.text = "$fileName\n$fileSize"
        Glide.with(this).load(imageUri).into(image_photo)
    }

    override fun setExifDataList(list: List<ExifTagsContainer>) {
        recycler_view.setHasFixedSize(true)
        val mLayoutManager = LinearLayoutManager(this)
        recycler_view.layoutManager = mLayoutManager
        val mAdapter = ExifFieldsAdapter(list, presenter)
        recycler_view.adapter = mAdapter
    }

    override fun setupUI(bitmap: Bitmap) {
        toolbar_layout.setExpandedTitleColor(resources.getColor(android.R.color.transparent))

        image_photo.setImageBitmap(bitmap)

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
            when (which) {
                0 -> {
                    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText(item.type.name, item.getOnStringProperties())
                    clipboard.primaryClip = clip

                    coordinator_layout.showSnackbar(R.string.text_copied_to_clipboard)
                }
                1 -> {
                    coordinator_layout.showSnackbar(R.string.not_implemented_yet)
                }
                2 -> {
                    val lat = item.list.find { it.tag == Constants.EXIF_LATITUDE }?.attribute?.toDouble()
                    val lng = item.list.find { it.tag == Constants.EXIF_LONGITUDE }?.attribute?.toDouble()

                    val fm = supportFragmentManager
                    val editNameDialogFragment = MapDialog.newInstance(lat ?: 0.0, lng ?: 0.0)
                    editNameDialogFragment.show(fm, "maps")
                }
            }
        })
        val dialog = alertDialogBuilder.create()
        dialog.show()
    }
}
