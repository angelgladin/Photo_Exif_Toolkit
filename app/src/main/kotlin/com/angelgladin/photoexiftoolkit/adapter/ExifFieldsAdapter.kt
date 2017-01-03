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


package com.angelgladin.photoexiftoolkit.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.angelgladin.photoexiftoolkit.R
import com.angelgladin.photoexiftoolkit.domain.ExifTagsContainer
import com.angelgladin.photoexiftoolkit.domain.Type
import com.angelgladin.photoexiftoolkit.presenter.PhotoDetailPresenter
import kotlinx.android.synthetic.main.item_exif_data.view.*

/**
 * Created on 12/22/16.
 */
class ExifFieldsAdapter(var exifList: List<ExifTagsContainer>, val presenter: PhotoDetailPresenter) : RecyclerView.Adapter<ExifFieldsAdapter.ViewHolder>() {

    val viewHolderReferenceList = mutableListOf<ViewHolder>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_exif_data, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = exifList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        viewHolderReferenceList.add(holder)
        holder.bind(exifList[position], presenter)
    }

    fun updateList(exifList: List<ExifTagsContainer>) {
        this.exifList = exifList
        notifyDataSetChanged()
    }

    fun setAddress(address: String) {
        viewHolderReferenceList[0].setAddress(address)
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: ExifTagsContainer, presenter: PhotoDetailPresenter) = with(itemView) {
            text_properties.text = item.getOnStringProperties()
            itemView.setOnClickListener { presenter.onItemPressed(item) }
            when (item.type) {
                Type.LOCATION_DATA -> {
                    image_type.setImageResource(R.drawable.ic_pin_drop_black_24dp)
                    text_type.text = resources.getString(R.string.item_location)
                }
                Type.CAMERA_PROPERTIES -> {
                    image_type.setImageResource(R.drawable.ic_photo_camera_black_24dp)
                    text_type.text = resources.getString(R.string.item_camera_properties)
                }
                Type.DATE -> {
                    image_type.setImageResource(R.drawable.ic_date_range_black_24dp)
                    text_type.text = resources.getString(R.string.item_date)
                }
                Type.DIMENSION -> {
                    image_type.setImageResource(R.drawable.ic_photo_size_select_actual_black_24dp)
                    text_type.text = resources.getString(R.string.item_dimension)
                }
                Type.OTHER -> {
                    image_type.setImageResource(R.drawable.ic_blur_on_black_24dp)
                    text_type.text = resources.getString(R.string.item_other)
                }
            }
        }

        fun setAddress(address: String) {
            itemView.text_address.visibility = View.VISIBLE
            itemView.text_address.text = address
        }
    }

}

