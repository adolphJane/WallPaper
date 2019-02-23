package com.magicalrice.adolph.wallpaper.utils.toast

import android.os.Build
import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.Toast
import java.lang.reflect.Field

class SystemToast : AbsToast {
    private var sField_mTN: Field? = null
    private var sField_TN_Handler: Field? = null

    constructor(toast: Toast) : super(toast) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {
            try {

                sField_mTN = Toast::class.java.getDeclaredField("mTN")
                sField_mTN?.isAccessible = true
                val mTN = sField_mTN?.get(toast)
                sField_TN_Handler = sField_mTN?.type?.getDeclaredField("mHandler")
                sField_TN_Handler?.isAccessible = true
                val tnHandler = sField_TN_Handler?.get(mTN) as Handler?
                sField_TN_Handler?.set(mTN, SafeHandler(tnHandler))
            } catch (ignored: Exception) {
            }

        }
    }

    override fun show() {
        mToast?.show()
    }

    override fun cancel() {
        mToast?.cancel()
    }

    internal class SafeHandler(private val impl: Handler?) : Handler() {

        override fun handleMessage(msg: Message) {
            impl?.handleMessage(msg)
        }

        override fun dispatchMessage(msg: Message) {
            try {
                impl?.dispatchMessage(msg)
            } catch (e: Exception) {
                Log.e("ToastUtils", e.toString())
            }

        }
    }
}