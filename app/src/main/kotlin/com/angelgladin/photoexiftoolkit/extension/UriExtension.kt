package com.angelgladin.photoexiftoolkit.extension

import android.content.Context
import android.net.Uri
import android.provider.MediaStore

/**
 * Created on 12/22/16.
 */
fun Uri.getPathFromUri(context: Context): String? {
  var filePath: String? = null
  val contentResolver = context.contentResolver

  val proj = arrayOf(MediaStore.Images.Media.DATA)
  val cursor = contentResolver.query(this, proj, null, null, null)
  if (cursor.moveToFirst()) {
    val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
    filePath = cursor.getString(column_index)
  }
  cursor.close()

  return filePath
}
