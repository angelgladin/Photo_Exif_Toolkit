package com.angelgladin.photoexiftoolkit.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.angelgladin.photoexiftoolkit.R

class HomeActivity : AppCompatActivity() {

  //private val toolbar by lazy { findViewById(R.id.toolbar) as Toolbar? }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }
}
