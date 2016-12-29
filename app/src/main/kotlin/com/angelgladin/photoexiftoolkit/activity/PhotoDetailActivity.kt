package com.angelgladin.photoexiftoolkit.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.angelgladin.photoexiftoolkit.R
import com.angelgladin.photoexiftoolkit.adapter.ExifFieldsAdapter
import com.angelgladin.photoexiftoolkit.dialog.MapDialog
import com.angelgladin.photoexiftoolkit.domain.ExifTagsContainer
import com.angelgladin.photoexiftoolkit.domain.Location
import com.angelgladin.photoexiftoolkit.domain.Type
import com.angelgladin.photoexiftoolkit.extension.showSnackbar
import com.angelgladin.photoexiftoolkit.presenter.PhotoDetailPresenter
import com.angelgladin.photoexiftoolkit.view.PhotoDetailView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_photo_detail.*
import kotlinx.android.synthetic.main.content_photo_detail.*


class PhotoDetailActivity : AppCompatActivity(), PhotoDetailView, MapDialog.DialogEvents {

    val presenter = PhotoDetailPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_detail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar_layout.setExpandedTitleColor(resources.getColor(android.R.color.transparent))

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
            presenter.shareData()
            true
        }
        R.id.action_clear_exif -> {
            presenter.clearExif()
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

    override fun showAlertDialogWhenItemIsPressed(item: ExifTagsContainer) {
        val alertDialogBuilder = AlertDialog.Builder(this)

        val optionsList = mutableListOf(resources.getString(R.string.alert_item_copy_to_clipboard),
                resources.getString(R.string.alert_item_edit))

        if (item.type == Type.LOCATION_DATA)
            optionsList.add(resources.getString(R.string.alert_item_open_map))
        else if (item.type == Type.LOCATION_DATA)
            optionsList.remove(resources.getString(R.string.alert_item_edit))

        alertDialogBuilder.setTitle(resources.getString(R.string.alert_select_an_action))
        alertDialogBuilder.setItems(optionsList.toTypedArray(), { dialog, which ->
            when (which) {
                0 -> presenter.copyDataToClipboard(item)
                1 -> when (item.type) {
                    Type.LOCATION_DATA -> presenter.editLocation(item)
                    Type.DATE -> presenter.editDate(item)
                    else -> presenter.editExifFieldsOpeningDialog(item)
                }
                2 -> presenter.openDialogMap(item)
            }
        })
        val dialog = alertDialogBuilder.create()
        dialog.show()
    }

    override fun copyDataToClipboard(item: ExifTagsContainer) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(item.type.name, item.getOnStringProperties())
        clipboard.primaryClip = clip

        coordinator_layout.showSnackbar(R.string.text_copied_to_clipboard)
    }

    override fun editLocation(item: ExifTagsContainer) {
        coordinator_layout.showSnackbar(R.string.not_implemented_yet)
    }

    override fun editDate(item: ExifTagsContainer) {
        coordinator_layout.showSnackbar(R.string.not_implemented_yet)
    }

    override fun editExifFieldsOpeningDialog(item: ExifTagsContainer) {
        coordinator_layout.showSnackbar(R.string.not_implemented_yet)
    }

    override fun openDialogMap(latitude: Double?, longitude: Double?) {
        val fm = supportFragmentManager
        val editNameDialogFragment = MapDialog.newInstance(latitude ?: 0.0, longitude ?: 0.0)
        editNameDialogFragment.show(fm, "maps")
    }

    override fun shareData(data: String) {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, data)
        sendIntent.type = "text/plain"
        startActivity(sendIntent)
    }

    override fun clearData(list: List<ExifTagsContainer>) {
        coordinator_layout.showSnackbar(R.string.not_implemented_yet)
    }

    override fun onError() {
        coordinator_layout.showSnackbar(R.string.not_implemented_yet)
    }

    override fun locationChanged(locationChanged: Boolean, location: Location) {
        Log.e("asdasd", "Location changed: $locationChanged, Location: $location")
    }
}
