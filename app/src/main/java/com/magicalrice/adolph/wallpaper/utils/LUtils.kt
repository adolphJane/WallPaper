package com.magicalrice.adolph.wallpaper.utils

import com.orhanobut.logger.Logger

object LUtils {
    fun <T> e(tag:String, arrayList: ArrayList<T>) {
        for (msg in arrayList) {
            e(tag,msg.toString())
        }
    }

    fun e(tag:String, message:String) {
        Logger.t(tag).e(message)
    }

    fun e(message: String) {
        Logger.e(message)
    }

    fun e(throwable: Throwable,message: String) {
        Logger.e(throwable,message)
    }

    fun d(message:String) {
        Logger.d(message)
    }
}