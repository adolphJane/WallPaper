package com.magicalrice.adolph.wallpaper.view.viewer

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Application
import android.app.WallpaperManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
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
import java.io.IOException
import java.lang.ref.WeakReference

class WallpaperViewerViewModel(application: Application) : BaseViewModel(application) {
    var isBarShow: Boolean = true
    val wallpaper = MutableLiveData<WallpaperCollectBean>()
    private var imgPath = ""
    private var isDownload: Boolean = false
    private var isDownloading: Boolean = false
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
            showCustomToast(activity, "取消收藏",false)
        } else {
            val bean = WallpaperCollectBean(type = type,imgPath = imgPath)
            RxUtils.doOnIOThread(object : IOTask {
                override fun doOnIO() {
                    dao.insertWallpaper(bean)
                }
            })
            showCustomToast(activity, "收藏成功",true)
        }
    }

    fun downloadWallpaper(activity: FragmentActivity, type: Int) {
        val symbol = imgPath.split("/").last().split(".").first()
        if (type == 1) {
            val appDir = File(activity.getExternalFilesDir(null), "wallpaper/desktop/$symbol.jpg")
            if (appDir.exists()) {
                showCustomToast(activity, "该壁纸已经保存",false)
                return
            }
        } else {
            val appDir = File(activity.getExternalFilesDir(null), "wallpaper/phone/$symbol.jpg")
            if (appDir.exists()) {
                showCustomToast(activity, "该壁纸已经保存",false)
                return
            }
        }
        if (!isDownloading) {
            isDownloading = true
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
                                showCustomToast(activity, "壁纸保存完成",true)
                                isDownloading = false
                            }
                        })
                    }

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        super.onLoadFailed(errorDrawable)
                        isDownloading = false
                    }
                })
        } else {
            showCustomToast(activity, "正在下载中...", false)
        }
    }

    fun deleteWallpaper(activity: FragmentActivity, path: String) {
        if (path.isNotEmpty()) {
            try {
                val file = File(path)
                if (file.exists()) {
                    file.delete()
                    showCustomToast(activity, "删除成功", true)
                    activity.finish()
                } else {
                    showCustomToast(activity, "图片不存在", false)
                }
            } catch (e: IOException) {
                showCustomToast(activity, "删除失败", false)
            }
        }
    }

    fun initAnimator(downloadBottomBar: View?, bottomBar: View?, isDownload: Boolean) {
        this.isDownload = isDownload
        downloadBottomBar?.let {
            it.post {
                val deleteBarHeight = it.height.toFloat()
                animatorDelete = ObjectAnimator.ofFloat(downloadBottomBar, "translationY", 0f, deleteBarHeight)
                    .setDuration(300)
                animatorDelete?.interpolator = AccelerateInterpolator()
                animatorDeleteRe = ObjectAnimator.ofFloat(downloadBottomBar, "translationY", deleteBarHeight, 0f)
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

    private fun showCustomToast(activity: FragmentActivity,content: String,isSuccess: Boolean) {
        val view = ToastCustomView(activity)
        view.setToastContent(content)
        view.setToastIcon(if (isSuccess) R.drawable.ic_success else R.drawable.ic_error, ToastCustomView.LEFT)
        ToastUtils.setGravity(Gravity.CENTER, 0, 0)
            .showCustomShort(view)
    }

    @SuppressLint("InlinedApi")
    fun setWallpaper(type: Int, activity: WallpaperViewerActivity?) {
        activity?.let {
            if (isDownload) {
                try {
                    val bitmap = BitmapFactory.decodeFile(imgPath)
                    if (Build.VERSION.SDK_INT >= 24) WallpaperUtils.getInstance().setCroppedBitmap(it,bitmap,if (type == 1) WallpaperManager.FLAG_LOCK else WallpaperManager.FLAG_SYSTEM) else WallpaperUtils.getInstance().setCroppedBitmap(it,bitmap)
                    dismissDialog()
                    showToast("设置壁纸成功")
                } catch (e: IOException) {
                    showToast("设置壁纸失败")
                }
            } else {
                SetWallpaperTask(WeakReference(it), imgPath, if (type == 1) WallpaperManager.FLAG_LOCK else WallpaperManager.FLAG_SYSTEM).execute()
            }
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