package com.magicalrice.adolph.wallpaper.view.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.magicalrice.adolph.wallpaper.utils.ToastUtils

open class BaseViewModel(application: Application) : AndroidViewModel(application) {

    fun showToast(content: String?) {
        if (content?.isEmpty() == false) {
            ToastUtils.showShort(content)
        }
    }
}