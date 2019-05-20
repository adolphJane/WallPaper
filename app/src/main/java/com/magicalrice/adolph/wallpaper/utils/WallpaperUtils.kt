package com.magicalrice.adolph.wallpaper.utils

import android.annotation.TargetApi
import android.app.Activity
import android.app.WallpaperManager
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.os.Build
import androidx.core.view.ViewCompat
import com.google.gson.internal.reflect.ReflectionAccessor
import com.magicalrice.adolph.wallpaper.R
import java.io.File
import java.io.FileInputStream
import java.io.IOException

/**
 * @package com.magicalrice.adolph.wallpaper.utils
 * @author Adolph
 * @date 2019-05-20 Mon
 * @description TODO
 */

class WallpaperUtils {

    fun darkenBitMap(bitmap: Bitmap?) : Bitmap {
        val createBitmap = Bitmap.createBitmap(bitmap)
        val canvas = Canvas(createBitmap)
        val paint = Paint(ViewCompat.MEASURED_STATE_MASK)
        canvas.drawBitmap(createBitmap, Matrix(), paint)
        return createBitmap
    }

    fun setCroppedBitmap(context: Context, bitmap: Bitmap?) : Boolean {
        if (bitmap == null) {
            return false
        }
        val instance = WallpaperManager.getInstance(context)
        try {
            instance.setBitmap(bitmap)
            instance.setWallpaperOffsetSteps(0f,0f)
            return true
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }
    }

    @TargetApi(24)
    fun setCroppedBitmap(context: Context, bitmap: Bitmap?, i: Int) : Boolean {
        if (Build.VERSION.SDK_INT >= 24 && bitmap != null) {
            val instance = WallpaperManager.getInstance(context)
            try {
                instance.setBitmap(bitmap,null,true,i)
                instance.setWallpaperOffsetSteps(0f,0f)
                return true
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        return false
    }

    fun setFullBitmap(context: Context, bitmap: Bitmap?) : Boolean {
        try {
            WallpaperManager.getInstance(context).setBitmap(bitmap)
            return true
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }
    }

    @TargetApi(24)
    fun setFullBitmap(context: Context, bitmap: Bitmap?, i: Int) : Boolean {
        if (Build.VERSION.SDK_INT < 24) {
            return false
        }

        val instance = WallpaperManager.getInstance(context)
        try {
            instance.setBitmap(bitmap, null, true, i)
            instance.setWallpaperOffsetSteps(0f,0f)
            return true
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }
    }

    fun setFullBitmap(context: Context, str: String) : Boolean {
        try {
            WallpaperManager.getInstance(context).setStream(FileInputStream(File(str)))
            return true
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }
    }

    fun setWallpaperUsingSystemAPI(activity: Activity, uri: Uri) {
        val intent = Intent("android.service.wallpaper.CROP_AND_SET_WALLPAPER")
        intent.setDataAndType(uri, "image/*")
        activity.startActivityForResult(Intent.createChooser(intent, activity.getString(R.string.set_wallpaper_desc)),REQUEST_SET_AS_WALLPAPER)
    }

    private object Holder {
        val INSTANCE = WallpaperUtils()
    }

    companion object {
        fun getInstance() = Holder.INSTANCE
        const val REQUEST_SET_AS_WALLPAPER = 1024
    }
}