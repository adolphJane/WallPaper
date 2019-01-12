package com.magicalrice.adolph.wallpaper

import android.app.Application
import com.magicalrice.adolph.wallpaper.BuildConfig
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.squareup.leakcanary.LeakCanary
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.bugly.crashreport.CrashReport.UserStrategy

/**
 * Created by Adolph on 2018/7/18.
 */
class WallpaperApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        initLogger()
        initLeakCanary()
        initBugly()
    }

    private fun initLogger() {
        val strategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)
                .methodCount(5)
                .methodOffset(6)
                .tag("AndroidTest:")
                .build()
        Logger.addLogAdapter(object : AndroidLogAdapter(strategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return true
            }
        })
    }

    private fun initLeakCanary() {
        if (!LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        LeakCanary.install(this)
    }

    private fun initBugly() {
        val strategy = UserStrategy(this)
        strategy.setAppChannel("guanfang")
        strategy.setAppVersion(BuildConfig.VERSION_NAME)
        strategy.setAppPackageName(BuildConfig.APPLICATION_ID)
        CrashReport.initCrashReport(applicationContext, "5d8177ce84", false)
    }

    companion object {
        lateinit var instance: WallpaperApplication
    }
}