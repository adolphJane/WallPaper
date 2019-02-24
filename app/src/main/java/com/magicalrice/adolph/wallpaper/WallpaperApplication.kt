package com.magicalrice.adolph.wallpaper

import android.app.Application
import android.arch.persistence.room.Room
import android.databinding.DataBindingComponent
import android.databinding.DataBindingUtil
import com.magicalrice.adolph.wallpaper.BuildConfig
import com.magicalrice.adolph.wallpaper.data.local.WallpaperDatabase
import com.magicalrice.adolph.wallpaper.utils.AppManager
import com.magicalrice.adolph.wallpaper.utils.BindUtils
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
    private lateinit var mWallpaperDatabase: WallpaperDatabase

    override fun onCreate() {
        super.onCreate()
        AppManager.getInstance().init(this)
        instance = this
        initLogger()
        initLeakCanary()
        initBugly()
        initDatabase()
        initDataBinding()
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

    fun getDatabase() : WallpaperDatabase {
        return mWallpaperDatabase
    }

    private fun initDatabase() {
        mWallpaperDatabase = Room.databaseBuilder(this,WallpaperDatabase::class.java,"wallpaper.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    private fun initDataBinding() {
        DataBindingUtil.setDefaultComponent(object : DataBindingComponent {
            override fun getBindUtils(): BindUtils {
                return BindUtils
            }
        })
    }

    companion object {
        lateinit var instance: WallpaperApplication
    }
}