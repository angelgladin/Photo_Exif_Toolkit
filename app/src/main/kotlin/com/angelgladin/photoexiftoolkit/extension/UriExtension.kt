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
