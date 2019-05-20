package com.magicalrice.adolph.wallpaper.view.local

import android.app.Application
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import com.magicalrice.adolph.wallpaper.WallpaperApplication
import com.magicalrice.adolph.wallpaper.bean.WallpaperCollectBean
import com.magicalrice.adolph.wallpaper.view.base.BaseViewModel
import java.io.File

/**
 * @package com.magicalrice.adolph.wallpaper.view.local
 * @author Adolph
 * @date 2019-05-20 Mon
 * @description TODO
 */

class LocalViewModel(application: Application) : BaseViewModel(application) {
    private val dao = (application as WallpaperApplication).getDatabase().wallpaperListDao()

    fun loadCollect(wallpaperType: Int): LiveData<List<WallpaperCollectBean>> {
        return dao.loadAllWallpaper(wallpaperType)
    }

    fun loadDownload(wallpaperType: Int, activity: FragmentActivity?): MutableList<File>? {
        if (activity != null) {
            if (wallpaperType == 1) {
                //加载下载桌面壁纸
                val appDir = File(activity.getExternalFilesDir(null), "wallpaper/desktop")
                if (!appDir.exists()) {
                    appDir.mkdirs()
                }
                return appDir.listFiles().toMutableList()
            } else if (wallpaperType == 2) {
                //加载下载手机壁纸
                val appDir = File(activity.getExternalFilesDir(null), "wallpaper/phone")
                if (!appDir.exists()) {
                    appDir.mkdirs()
                }
                return appDir.listFiles().toMutableList()
            }
        }
        return null
    }
}