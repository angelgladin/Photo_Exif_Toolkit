package com.angelgladin.photoexiftoolkit.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.angelgladin.photoexiftoolkit.R
import com.angelgladin.photoexiftoolkit.domain.ExifTagsContainer
import com.angelgladin.photoexiftoolkit.domain.Type
import kotlinx.android.synthetic.main.item_exif_data.view.*

/**
 * Created on 12/22/16.
 */
class ExifFieldsAdapter(val exifList: List<ExifTagsContainer>) : RecyclerView.Adapter<ExifFieldsAdapter.ViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_exif_data, parent, false)
    return ViewHolder(view)
  }

  override fun getItemCount(): Int = exifList.size

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(exifList[position])
  }

  class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(item: ExifTagsContainer) = with(itemView) {
      text.text = item.list.toString()
      when (item.type) {
        Type.LOCATION_DATA -> image_type.setImageResource(R.mipmap.ic_launcher)
        Type.CAMERA_PROPERTIES -> image_type.setImageResource(R.mipmap.ic_launcher)
        Type.DATE -> image_type.setImageResource(R.mipmap.ic_launcher)
        Type.DIMENSION -> image_type.setImageResource(R.mipmap.ic_launcher)
        Type.OTHER -> image_type.setImageResource(R.mipmap.ic_launcher)
      }
    }
  }

}
