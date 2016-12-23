package com.angelgladin.photoexiftoolkit.activity

import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.graphics.Palette
import android.view.Menu
import com.angelgladin.photoexiftoolkit.R
import kotlinx.android.synthetic.main.activity_photo_detail.*

class PhotoDetailActivity : AppCompatActivity() {

  val path = "/storage/emulated/0/DCIM/Camera/IMG_20161222_162439.jpg"

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_photo_detail)
    setSupportActionBar(toolbar)

    /*
    val list = intent.getSerializableExtra("list") as ArrayList<ExifField>
    System.out.println(list.toString())
    val availableLocation = intent.getBooleanExtra("available_location");
    */
    toolbar_layout.setExpandedTitleColor(resources.getColor(android.R.color.transparent))
    val bitmap = BitmapFactory.decodeFile(path)
    image_photo.setImageBitmap(bitmap)

    val palette = Palette.from(bitmap).generate()
    // Pick one of the swatches
    val vibrant = palette.dominantSwatch;
    if (vibrant != null) {
      toolbar_layout.setContentScrimColor(vibrant.rgb)
    }


  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_photo_detail, menu)
    return super.onCreateOptionsMenu(menu)
  }
}
