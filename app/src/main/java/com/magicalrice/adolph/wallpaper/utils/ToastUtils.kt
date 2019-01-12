package com.magicalrice.adolph.wallpaper.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.support.annotation.StringRes
import android.widget.Toast

@SuppressLint("ShowToast")
object ToastUtils {
    private var toast: Toast? = null
    private var handler: Handler = Handler(Looper.getMainLooper())

    fun showToast(context: Context?, content: String?) {
        if (Thread.currentThread() == Looper.getMainLooper().thread) {
            if (toast == null) {
                toast = Toast.makeText(context, content, Toast.LENGTH_SHORT)
                toast?.show()
            } else {
                toast?.setText(content)
                toast?.show()
            }
        } else {
            handler.post {
                if (toast == null) {
                    toast = Toast.makeText(context, content, Toast.LENGTH_SHORT)
                    toast?.show()
                } else {
                    toast?.setText(content)
                    toast?.show()
                }
            }
        }
    }

    fun showToast(context: Context?, @StringRes resId: Int) {
        showToast(context, context?.getString(resId))
    }
}