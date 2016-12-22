package com.angelgladin.photoexiftoolkit.common

/**
 * Created on 12/22/16.
 */
interface BasePresenter<out V> {
  val view: V
  fun initialize()
}