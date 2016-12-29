package com.angelgladin.photoexiftoolkit.extension

import android.support.annotation.StringRes
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar


/**
 * Created on 12/22/16.
 */
fun CoordinatorLayout.showSnackbar(@StringRes text: Int) {
    val snackbar = Snackbar.make(this, context.getText(text), Snackbar.LENGTH_LONG)
    snackbar.show()
}

fun CoordinatorLayout.showSnackbar(text: String) {
    val snackbar = Snackbar.make(this, text, Snackbar.LENGTH_LONG)
    snackbar.show()
}