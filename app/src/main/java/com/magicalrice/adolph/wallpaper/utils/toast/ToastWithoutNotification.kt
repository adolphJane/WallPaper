package com.magicalrice.adolph.wallpaper.utils.toast

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.LayoutDirection
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.magicalrice.adolph.wallpaper.utils.AppManager

class ToastWithoutNotification : AbsToast {
    private val handler = Handler(Looper.getMainLooper())

    private var mWM: WindowManager? = null
    private var mView: View? = null

    private val mParams = WindowManager.LayoutParams()

    constructor(toast: Toast): super(toast) {
    }

    override fun show() {
        mView = mToast?.getView()
        if (mView == null) return
        val context = mToast?.view?.context
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N_MR1) {
            mWM = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            mParams.type = WindowManager.LayoutParams.TYPE_TOAST
            mParams.y = mToast?.yOffset ?: 0
        } else {
            val topActivityOrApp = AppManager.getInstance().getTopActivityOrApp()
            if (topActivityOrApp is Activity) {
                val topActivity = topActivityOrApp
                mWM = topActivity.windowManager
            }
            mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL
            mParams.y = mToast?.yOffset ?: 0 + getNavBarHeight()
        }

        val config = context?.resources?.configuration
        val gravity: Int
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            gravity = Gravity.getAbsoluteGravity(mToast?.gravity ?: Gravity.CENTER, config?.layoutDirection ?: LayoutDirection.LTR)
        } else {
            gravity = mToast?.gravity ?: Gravity.CENTER
        }

        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT
        mParams.format = PixelFormat.TRANSLUCENT
        mParams.windowAnimations = android.R.style.Animation_Toast

        mParams.title = "ToastWithoutNotification"
        mParams.flags = (WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        mParams.gravity = gravity
        if (gravity and Gravity.HORIZONTAL_GRAVITY_MASK == Gravity.FILL_HORIZONTAL) {
            mParams.horizontalWeight = 1.0f
        }
        if (gravity and Gravity.VERTICAL_GRAVITY_MASK == Gravity.FILL_VERTICAL) {
            mParams.verticalWeight = 1.0f
        }
        mParams.x = mToast?.xOffset ?: 0
        mParams.packageName = AppManager.getInstance().getApp().packageName

        try {
            mWM?.addView(mView, mParams)
        } catch (ignored: Exception) {
        }

        handler.postDelayed({
            cancel()
        }, (if (mToast?.duration == Toast.LENGTH_SHORT) 2000 else 3500).toLong())
    }

    override fun cancel() {
        try {
            if (mWM != null) {
                mWM!!.removeViewImmediate(mView)
            }
        } catch (ignored: Exception) {
        }

        mView = null
        mWM = null
        mToast = null
    }

    private fun getNavBarHeight(): Int {
        val res = Resources.getSystem()
        val resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId != 0) {
            res.getDimensionPixelSize(resourceId)
        } else {
            0
        }
    }
}