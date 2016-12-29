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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_exif_data, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = exifList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
            holder.bind(exifList[position], presenter)

    fun updateList(exifList: List<ExifTagsContainer>) {
        this.exifList = exifList
        notifyDataSetChanged()
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
                    text_type.text = resources.getString(R.string.item_date)
                }
                Type.DATE -> {
                    image_type.setImageResource(R.drawable.ic_date_range_black_24dp)
                    text_type.text = resources.getString(R.string.item_camera_properties)
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
    }

}
