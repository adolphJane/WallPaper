package com.magicalrice.adolph.wallpaper.utils

import android.content.Context

object ScreenUtils {
    fun dp2px(context: Context?,dpValue: Float) : Int {
        val scale = context?.resources?.displayMetrics?.density ?: 0f
        return (dpValue * scale + 0.5f).toInt()
    }

    fun px2dp(context: Context,pxValue: Float) : Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    fun sp2px(context: Context,spValue: Float) : Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    fun px2sp(context: Context,pxValue: Float) : Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }

    fun getScreenWidth(context: Context) : Int{
        return context.resources.displayMetrics.widthPixels
    }

    fun getScreenHeight(context: Context) : Int{
        return context.resources.displayMetrics.heightPixels
    }

    fun getStatusBarHeight(context: Context) : Int{
        var statusBarHeight = -1
        val resourceId = context.resources.getIdentifier("status_bar_height","dimen","android")
        if (resourceId > 0) {
            statusBarHeight = context.resources.getDimensionPixelSize(resourceId)
        }
        return statusBarHeight
    }
}