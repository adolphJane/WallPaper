package com.magicalrice.adolph.wallpaper.utils.toast

import android.view.View
import android.widget.Toast

abstract class AbsToast : IToast {
    protected var mToast: Toast? = null

    constructor(toast: Toast?) {
        this.mToast = toast
    }

    override fun setView(view: View) {
        mToast?.view = view
    }

    override fun getView(): View? {
        return mToast?.view
    }

    override fun setDuration(duration: Int) {
        mToast?.duration = duration
    }

    override fun setGravity(gravity: Int, xOffset: Int, yOffset: Int) {
        mToast?.setGravity(gravity, xOffset, yOffset)
    }

    override fun setText(resId: Int) {
        mToast?.setText(resId)
    }

    override fun setText(s: CharSequence) {
        mToast?.setText(s)
    }
}