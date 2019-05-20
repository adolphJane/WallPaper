package com.magicalrice.adolph.wallpaper.view.viewer

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
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
import com.magicalrice.adolph.wallpaper.utils.*
import com.magicalrice.adolph.wallpaper.view.base.BaseViewModel
import com.magicalrice.adolph.wallpaper.widget.GlideApp
import com.magicalrice.adolph.wallpaper.widget.ToastCustomView
import java.io.File
import java.lang.ref.WeakReference

class WallpaperViewerViewModel(application: Application) : BaseViewModel(application) {
    var isBarShow: Boolean = true
    val wallpaper = MutableLiveData<WallpaperCollectBean>()
    private var imgPath = ""
    private var isDownload: Boolean = false
    private var animatorDelete: ObjectAnimator? = null
    private var animatorBottom: ObjectAnimator? = null
    private var animatorDeleteRe: ObjectAnimator? = null
    private var animatorBottomRe: ObjectAnimator? = null
    private var dialogFragment: WallpaperBrowserDialogFragment? = null
    private val dao = (application as WallpaperApplication).getDatabase().wallpaperListDao()

    fun loadCurrentWallpaper(imgUrl: String,activity: FragmentActivity) {
        this.imgPath = imgUrl
        dao.findWallpaper(imgUrl).observe(activity, Observer {
            wallpaper.value = it?.firstOrNull()
        })
    }

    fun openBrowser(activity: FragmentActivity) {
        dialogFragment = WallpaperBrowserDialogFragment.start(activity)
    }

    fun collectWallpaper(activity: FragmentActivity,type: Int) {
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
            val bean = WallpaperCollectBean(type = type,imgPath = imgPath)
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

    fun downloadWallpaper(activity: FragmentActivity, type: Int) {
        val symbol = imgPath.split("/").last().split(".").first()
        if (type == 1) {
            val appDir = File(activity.getExternalFilesDir(null), "wallpaper/desktop/$symbol.jpg")
            if (appDir.exists()) {
                showToast("该壁纸已经保存")
                return
            }
        } else {
            val appDir = File(activity.getExternalFilesDir(null), "wallpaper/phone/$symbol.jpg")
            if (appDir.exists()) {
                showToast("该壁纸已经保存")
                return
            }
        }
        GlideApp.with(activity)
            .asBitmap()
            .load(imgPath)
            .into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    BitmapUtils.saveImageToGallery(activity,if (type == 1) "wallpaper/desktop" else "wallpaper/phone",resource, symbol, object : ImageFinishListener {
                        override fun imgFinish(path: String) {
                            showToast("壁纸保存完成")
                        }
                    })
                }
            })
    }

    fun initAnimator(tvDelete: View?, bottomBar: View?, isDownload: Boolean) {
        this.isDownload = isDownload
        tvDelete?.let {
            it.post {
                val deleteBarHeight = it.height.toFloat()
                animatorDelete = ObjectAnimator.ofFloat(tvDelete, "translationY", 0f, deleteBarHeight)
                    .setDuration(300)
                animatorDelete?.interpolator = AccelerateInterpolator()
                animatorDeleteRe = ObjectAnimator.ofFloat(tvDelete, "translationY", deleteBarHeight, 0f)
                    .setDuration(300)
                animatorDeleteRe?.interpolator = AccelerateInterpolator()
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
        if (isBarShow) {
            if (isDownload) {
                if (animatorDelete?.isRunning == false) {
                    animatorDelete?.start()
                }
            } else {
                if (animatorBottom?.isRunning == false) {
                    animatorBottom?.start()
                }
            }
            isBarShow = false
        } else {
            if (isDownload) {
                if (animatorDeleteRe?.isRunning == false) {
                    animatorDeleteRe?.start()
                }
            } else {
                if (animatorBottomRe?.isRunning == false) {
                    animatorBottomRe?.start()
                }
            }
            isBarShow = true
        }
    }

    @SuppressLint("InlinedApi")
    fun setWallpaper(type: Int, activity: WallpaperViewerActivity?) {
        activity?.let {
            SetWallpaperTask(WeakReference(it), imgPath, if (type == 1) WallpaperManager.FLAG_LOCK else WallpaperManager.FLAG_SYSTEM).execute()
        }
    }

    fun dismissDialog() {
        dialogFragment?.dismissAllowingStateLoss()
    }

    fun dismiss() {
        animatorDelete?.cancel()
        animatorDelete = null

        animatorDeleteRe?.cancel()
        animatorDeleteRe = null

        animatorBottom?.cancel()
        animatorBottom = null

        animatorBottomRe?.cancel()
        animatorBottomRe = null
    }
}