package com.magicalrice.adolph.wallpaper.view.viewer

import android.animation.ObjectAnimator
import android.app.Application
import android.app.WallpaperManager
import android.graphics.Bitmap
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.magicalrice.adolph.wallpaper.R
import com.magicalrice.adolph.wallpaper.WallpaperApplication
import com.magicalrice.adolph.wallpaper.bean.WallpaperCollectBean
import com.magicalrice.adolph.wallpaper.utils.IOTask
import com.magicalrice.adolph.wallpaper.utils.RxUtils
import com.magicalrice.adolph.wallpaper.utils.ToastUtils
import com.magicalrice.adolph.wallpaper.view.base.BaseViewModel
import com.magicalrice.adolph.wallpaper.widget.GlideApp
import com.magicalrice.adolph.wallpaper.widget.ToastCustomView

class WallpaperViewerViewModel(application: Application) : BaseViewModel(application) {
    var isToolbarShow: Boolean = true
    val wallpaper = MutableLiveData<WallpaperCollectBean>()
    private var imgPath = ""
    private var animatorTop: ObjectAnimator? = null
    private var animatorBottom: ObjectAnimator? = null
    private var animatorTopRe: ObjectAnimator? = null
    private var animatorBottomRe: ObjectAnimator? = null
    private val dao = (application as WallpaperApplication).getDatabase().wallpaperListDao()

    fun loadCurrentWallpaper(imgUrl: String,activity: FragmentActivity) {
        this.imgPath = imgUrl
        dao.findWallpaper(imgUrl).observe(activity, Observer {
            wallpaper.value = it?.firstOrNull()
        })
    }

    fun openBrowser(activity: FragmentActivity) {
        WallpaperBrowserDialogFragment.start(activity)
    }

    fun collectWallpaper(activity: FragmentActivity) {
        if (wallpaper.value != null) {

            RxUtils.doOnIOThread(object : IOTask {
                override fun doOnIO() {
                    dao.deleteWallpaer(wallpaper.value!!)
                }
            })
            val view = ToastCustomView(activity)
            view.setToastContent("取消收藏")
            view.setToastIcon(R.drawable.ic_success, ToastCustomView.LEFT)
            ToastUtils.setGravity(Gravity.CENTER, 0, 0)
                .showCustomShort(view)
        } else {
            val bean = WallpaperCollectBean(imgPath = imgPath)
            RxUtils.doOnIOThread(object : IOTask {
                override fun doOnIO() {
                    dao.insertWallpaper(bean)
                }
            })
            val view = ToastCustomView(activity)
            view.setToastContent("收藏成功")
            view.setToastIcon(R.drawable.ic_success, ToastCustomView.LEFT)
            ToastUtils.setGravity(Gravity.CENTER, 0, 0)
                .showCustomShort(view)
        }
    }

    fun downloadWallpaper(activity: FragmentActivity) {
        GlideApp.with(activity)
            .asBitmap()
            .load(imgPath)
            .into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {

                }
            })
    }

    fun initAnimator(topbar: View?, bottomBar: View?) {
        topbar?.let {
            it.post {
                val topBarHeight = it.height.toFloat()
                animatorTop = ObjectAnimator.ofFloat(topbar, "translationY", 0f, topBarHeight)
                    .setDuration(300)
                animatorTop?.interpolator = AccelerateInterpolator()
                animatorTopRe = ObjectAnimator.ofFloat(topbar, "translationY", topBarHeight, 0f)
                    .setDuration(300)
                animatorTopRe?.interpolator = AccelerateInterpolator()
            }
        }
        bottomBar?.let {
            it.post {
                val bottomBarHeight = it.height.toFloat()
                animatorBottom =
                    ObjectAnimator.ofFloat(bottomBar, "translationY", 0f, bottomBarHeight)
                        .setDuration(300)
                animatorBottom?.interpolator = AccelerateInterpolator()

                animatorBottomRe =
                    ObjectAnimator.ofFloat(bottomBar, "translationY", bottomBarHeight, 0f)
                        .setDuration(300)
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

    fun setWallpaper(type: Int,activity: FragmentActivity?) {
        activity?.let {
            val wallpaperManager = WallpaperManager.getInstance(it)
            try {
                GlideApp.with(it)
                    .asBitmap()
                    .load(imgPath)
                    .into(object : SimpleTarget<Bitmap>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                wallpaperManager.setBitmap(resource,null,true,if (type == 1) WallpaperManager.FLAG_LOCK  else WallpaperManager.FLAG_SYSTEM)
                            } else {
                                wallpaperManager.setBitmap(resource)
                            }
                        }
                    })
            } catch (e: Exception) {
                e.printStackTrace()
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