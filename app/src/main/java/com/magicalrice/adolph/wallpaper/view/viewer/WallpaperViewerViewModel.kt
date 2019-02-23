package com.magicalrice.adolph.wallpaper.view.viewer

import android.animation.ObjectAnimator
import android.app.Application
import android.support.v4.app.FragmentActivity
import android.view.Gravity
import android.view.View
import android.view.animation.AccelerateInterpolator
import com.magicalrice.adolph.wallpaper.R
import com.magicalrice.adolph.wallpaper.utils.ToastUtils
import com.magicalrice.adolph.wallpaper.view.base.BaseViewModel
import com.magicalrice.adolph.wallpaper.widget.ToastCustomView

class WallpaperViewerViewModel(application: Application) : BaseViewModel(application) {
    var isToolbarShow: Boolean = true
    private var animatorTop: ObjectAnimator? = null
    private var animatorBottom: ObjectAnimator? = null
    private var animatorTopRe: ObjectAnimator? = null
    private var animatorBottomRe: ObjectAnimator? = null

    fun openBrowser(activity: FragmentActivity) {
        WallpaperBrowserDialogFragment.start(activity)
    }

    fun collectWallpaper(activity: FragmentActivity) {
        val view = ToastCustomView(activity)
        view.setToastContent("收藏成功")
        view.setToastIcon(R.drawable.ic_success, ToastCustomView.LEFT)
        ToastUtils.setGravity(Gravity.CENTER, 0, 0)
            .showCustomShort(view)
    }

    fun downloadWallpaper() {

    }

    fun initAnimator(topbar: View?, bottomBar: View?) {
        topbar?.let {
            it.post {
                val topBarHeight = it.height.toFloat()
                animatorTop = ObjectAnimator.ofFloat(topbar, "translationY", 0f, topBarHeight)
                    .setDuration(500)
                animatorTop?.interpolator = AccelerateInterpolator()
                animatorTopRe = ObjectAnimator.ofFloat(topbar, "translationY", topBarHeight, 0f)
                    .setDuration(500)
                animatorTopRe?.interpolator = AccelerateInterpolator()
            }
        }
        bottomBar?.let {
            it.post {
                val bottomBarHeight = it.height.toFloat()
                animatorBottom =
                    ObjectAnimator.ofFloat(bottomBar, "translationY", 0f, bottomBarHeight)
                        .setDuration(500)
                animatorBottom?.interpolator = AccelerateInterpolator()

                animatorBottomRe =
                    ObjectAnimator.ofFloat(bottomBar, "translationY", bottomBarHeight, 0f)
                        .setDuration(500)
                animatorBottomRe?.interpolator = AccelerateInterpolator()

            }
        }
    }

    fun showWallpaper() {
        if (isToolbarShow) {
            if (animatorTop?.isRunning == false || animatorBottom?.isRunning == false) {
                animatorTop?.start()
                animatorBottom?.start()
                isToolbarShow = false
            }
        } else {
            if (animatorTopRe?.isRunning == false || animatorBottomRe?.isRunning == false) {
                animatorTopRe?.start()
                animatorBottomRe?.start()
                isToolbarShow = true
            }
        }
    }

    fun dismiss() {
        animatorTop?.cancel()
        animatorTop = null

        animatorTopRe?.cancel()
        animatorTopRe = null

        animatorBottom?.cancel()
        animatorBottom = null

        animatorBottomRe?.cancel()
        animatorBottomRe = null
    }
}