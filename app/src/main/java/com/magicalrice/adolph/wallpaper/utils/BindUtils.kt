package com.magicalrice.adolph.wallpaper.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter

object BindUtils {
    @BindingAdapter("isSelected")
    fun setSelected(view: TextView, isSelected: Boolean?) {
        isSelected?.let {
            view.isSelected = it
        }
    }
}