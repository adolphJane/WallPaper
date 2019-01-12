package com.magicalrice.adolph.wallpaper.utils

import android.os.Handler
import android.os.Looper

class MainLooper constructor(looper: Looper) : Handler(looper) {
    private object Holder {
        val INSTANCE = MainLooper(Looper.getMainLooper())
    }

    companion object {
        fun getInstance() = Holder.INSTANCE
    }

    fun runOnUiThread(runnable: Runnable) {
        if (Looper.getMainLooper().equals(Looper.myLooper())) {
            runnable.run()
        } else {
            getInstance().post(runnable)
        }
    }
}