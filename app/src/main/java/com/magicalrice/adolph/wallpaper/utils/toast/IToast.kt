package com.magicalrice.adolph.wallpaper.utils.toast

import android.view.View
import androidx.annotation.StringRes

interface IToast {
    fun show()
    fun cancel()
    fun setDuration(duration: Int)
    fun setGravity(gravity: Int, xOffset: Int, yOffset: Int)
    fun setText(@StringRes resId: Int)
    fun setText(s: CharSequence)
    fun getView(): View?
    fun setView(view: View)
}