package com.angelgladin.photoexiftoolkit.common

import android.content.Context

/**
 * Created on 12/22/16.
 */
interface BaseView {
  fun destroy()
  fun getContext(): Context
}