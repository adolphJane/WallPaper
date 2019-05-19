package com.magicalrice.adolph.wallpaper.widget

import android.content.Context
import android.view.LayoutInflater
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.magicalrice.adolph.wallpaper.R

class ToastCustomView : RelativeLayout {
    private lateinit var tvToast: TextView

    constructor(context: Context) : super(context) {
        init()
    }

    private fun init() {
        LayoutInflater.from(context).inflate(R.layout.layout_toast_show,this,true)
        tvToast = findViewById(R.id.tv_toast)
    }

    fun setToastContent(content: String) {
        tvToast.text = content
    }

    fun setToastIcon(@DrawableRes icon: Int, pos: Int) {
        val drawable = ContextCompat.getDrawable(context,icon)
        drawable?.setBounds(0,0,drawable.intrinsicWidth,drawable.intrinsicHeight)
        when (pos) {
            LEFT -> tvToast.setCompoundDrawables(drawable,null,null,null)
            TOP -> tvToast.setCompoundDrawables(null,drawable,null,null)
            BOTTOM -> tvToast.setCompoundDrawables(null,null,null,drawable)
            RIGHT -> tvToast.setCompoundDrawables(null,null,drawable,null)
        }
    }

    companion object {
        val LEFT = 1
        val TOP = 2
        val BOTTOM = 3
        val RIGHT = 4
    }
}