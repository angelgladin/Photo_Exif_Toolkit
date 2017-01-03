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

import java.io.File

/**
 * Created on 12/23/16.
 */
fun File.getSize(size: Size = Size.MB): String {
    val lengthBytes = this.length()
    return when (size) {
        Size.B -> "$lengthBytes"
        Size.KB -> "${lengthBytes / 1024} KB"
        Size.MB -> {
            val str = (lengthBytes / 1024).toString()
            return when (str.length) {
                1 -> "0.00$str MB"
                2 -> "0.0$str MB"
                3 -> "0.$str MB"
                else -> "${str.substring(0, str.length - 3)}.${str.substring(str.length - 3,
                        str.length)} MB"
            }
        }
    }
}

enum class Size { B, KB, MB }