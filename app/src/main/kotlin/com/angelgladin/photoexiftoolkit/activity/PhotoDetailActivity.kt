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

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.DatePicker
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


class PhotoDetailActivity : AppCompatActivity(), PhotoDetailView, DatePickerDialog.OnDateSetListener,
        MapDialog.DialogEvents {

    val presenter = PhotoDetailPresenter(this)

    val progressDialog: ProgressDialog by lazy {
        ProgressDialog(this).apply {
            setMessage(context.resources.getString(R.string.progress_dialog_address_loading))
            setProgressStyle(ProgressDialog.STYLE_SPINNER)
            isIndeterminate = true
        }
    }
    lateinit var recyclerViewAdapter: ExifFieldsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_detail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar_layout.setExpandedTitleColor(resources.getColor(android.R.color.transparent))

        presenter.initialize(intent)
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
        R.id.action_remove_tags -> {
            showAlertDialog(title = R.string.dialog_remove_tags_question,
                    message = R.string.dialog_remove_tags_caveat,
                    positiveButtonAction = {
                        presenter.removeAllTags()
                    },
                    negativeButtonAction = {})
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
        recyclerViewAdapter = ExifFieldsAdapter(list, presenter)
        recycler_view.adapter = recyclerViewAdapter
    }

    override fun showAddressOnRecyclerViewItem(address: String) {
        recyclerViewAdapter.setAddress(address)
    }

    override fun hideAddressOnRecyclerViewItem() {
        recyclerViewAdapter.hideAddress()
    }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun hideProgressDialog() {
        progressDialog.dismiss()
    }

    override fun changeExifDataList(list: List<ExifTagsContainer>) {
        recyclerViewAdapter.updateList(list)
    }

    override fun showAlertDialogWhenItemIsPressed(item: ExifTagsContainer) {
        val alertDialogBuilder = AlertDialog.Builder(this)

        val optionsList = mutableListOf<String>()
        //Add menu options to the alert dialog, the first one is in every item.
        optionsList.add(resources.getString(R.string.alert_item_copy_to_clipboard))
        if (item.type == Type.GPS) {
            optionsList.add(resources.getString(R.string.alert_item_open_map))
            optionsList.add(resources.getString(R.string.alert_item_remove_gps_tags))
        } else if (item.type == Type.DATE) {
            optionsList.add(resources.getString(R.string.alert_item_edit))
        }
        alertDialogBuilder.setTitle(resources.getString(R.string.alert_select_an_action))
        alertDialogBuilder.setItems(optionsList.toTypedArray(), { _, which ->
            if (which == 0) {
                presenter.copyDataToClipboard(item)
            } else if (which == 1) {
                if (item.type == Type.GPS)
                    presenter.openDialogMap(item)
                else if (item.type == Type.DATE)
                    presenter.editDate(item)
            } else if (which == 2 && item.type == Type.GPS) {
                showAlertDialog(title = R.string.dialog_remove_gps_tags_question,
                        message = R.string.dialog_remove_tags_caveat,
                        positiveButtonAction = {
                            presenter.removeTags(item.list.map { it.tag }.toSet())
                        },
                        negativeButtonAction = {})
            }
        })
        val dialog = alertDialogBuilder.create()
        dialog.show()
    }

    override fun copyDataToClipboard(item: ExifTagsContainer) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(item.type.name, item.getOnStringProperties())
        clipboard.primaryClip = clip

        coordinator_layout.showSnackbar(R.string.text_copied_to_clipboard_message)
    }

    override fun openDialogMap(latitude: Double?, longitude: Double?) {
        val fm = supportFragmentManager
        val editNameDialogFragment = MapDialog.newInstance(latitude ?: 0.0, longitude ?: 0.0)
        editNameDialogFragment.show(fm, "maps")
    }

    override fun showDialogEditDate(year: Int, month: Int, day: Int) {
        val datePickerDialog = DatePickerDialog(getContext(), this, year, month, day)
        datePickerDialog.show()
    }

    override fun shareData(data: String) {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, data)
        sendIntent.type = "text/plain"
        startActivity(sendIntent)
    }

    override fun onCompleteLocationChanged() {
        coordinator_layout.showSnackbar(R.string.location_changed_message)
    }

    override fun onCompleteDateChanged() {
        coordinator_layout.showSnackbar(R.string.date_changed_message)
    }

    override fun onError(message: String) {
        coordinator_layout.showSnackbar(message)
    }

    override fun onSuccessTagsDeleted(message: String) {
        coordinator_layout.showSnackbar(message)
    }

    override fun locationChanged(locationChanged: Boolean, location: Location) {
        if (locationChanged)
            presenter.changeExifLocation(location)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        presenter.changeExifDate(year, month, dayOfMonth)
    }

    inline private fun showAlertDialog(@StringRes title: Int,
                                       @StringRes message: Int,
                                       crossinline positiveButtonAction: () -> Unit,
                                       crossinline negativeButtonAction: () -> Unit) {
        AlertDialog.Builder(this)
                .setTitle(resources.getString(title))
                .setMessage(resources.getString(message))
                .setPositiveButton(resources.getString(android.R.string.ok),
                        { dialog, _ ->
                            positiveButtonAction()
                            dialog.dismiss()
                        })
                .setNegativeButton(resources.getString(android.R.string.cancel),
                        { dialog, _ ->
                            negativeButtonAction()
                            dialog.dismiss()
                        })
                .show()
    }
}
