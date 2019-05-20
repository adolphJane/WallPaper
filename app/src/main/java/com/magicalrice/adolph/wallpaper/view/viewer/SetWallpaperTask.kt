package com.magicalrice.adolph.wallpaper.view.viewer

import android.app.Activity
import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Build
import com.magicalrice.adolph.wallpaper.utils.ToastUtils
import com.magicalrice.adolph.wallpaper.utils.WallpaperUtils
import com.magicalrice.adolph.wallpaper.widget.GlideApp
import java.io.File
import java.lang.Exception
import java.lang.ref.WeakReference

/**
 * @package com.magicalrice.adolph.wallpaper.view.viewer
 * @author Adolph
 * @date 2019-05-20 Mon
 * @description TODO
 */

class SetWallpaperTask : AsyncTask<String, String, Boolean> {
    private var weakActivity: WeakReference<WallpaperViewerActivity>
    private var url: String
    private var flag: Int



    constructor(weakReference: WeakReference<WallpaperViewerActivity>, url: String, flag: Int) : super() {
        this.url = url
        this.weakActivity = weakReference
        this.flag = flag
    }

    override fun doInBackground(vararg params: String?): Boolean {
        weakActivity.get()?.let {
            try {
                val bitmap: Bitmap = GlideApp.with(it).asBitmap().load(url).submit(Int.MIN_VALUE,Int.MIN_VALUE).get()
                val croppedBitmap = if (Build.VERSION.SDK_INT >= 24) WallpaperUtils.getInstance().setCroppedBitmap(it,bitmap,this.flag) else WallpaperUtils.getInstance().setCroppedBitmap(it,bitmap)
                bitmap.recycle()
                return croppedBitmap
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            }
        }
        return false
    }

    override fun onPostExecute(result: Boolean?) {
        super.onPostExecute(result)
        val activity = weakActivity.get()
        if (activity != null && !activity.isDestroyed) {
            if (result == true) {
                ToastUtils.showShort("设置壁纸成功")
            } else {
                ToastUtils.showShort("设置壁纸失败")
            }
            activity.dismissBrowser()
        }
    }

    override fun onPreExecute() {
        super.onPreExecute()
        val activity = weakActivity.get()
        if (activity != null && !activity.isDestroyed) {
            ToastUtils.showShort("开始设置壁纸")
        }
    }
}