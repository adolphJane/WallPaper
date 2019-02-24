package com.magicalrice.adolph.wallpaper.utils

import android.databinding.BindingAdapter
import android.widget.TextView

object BindUtils {
    @BindingAdapter("isSelected")
    fun setSelected(view: TextView, isSelected: Boolean?) {
        isSelected?.let {
            view.isSelected = it
        }
    }
}