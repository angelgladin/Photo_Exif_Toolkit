package com.angelgladin.photoexiftoolkit.contract

import com.angelgladin.photoexiftoolkit.common.BasePresenter
import com.angelgladin.photoexiftoolkit.common.BaseView

/**
 * Created on 12/22/16.
 */
interface HomeContract {

  interface View : BaseView {

  }

  interface Presenter : BasePresenter<BaseView> {

  }
}