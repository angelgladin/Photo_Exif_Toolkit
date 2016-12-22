package com.angelgladin.photoexiftoolkit.common

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity

/**
 * Created on 12/22/16.
 */
abstract class BaseActivity: AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
    super.onCreate(savedInstanceState, persistentState)

  }
}