package com.magicalrice.adolph.wallpaper.utils

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import java.util.*

class AppManager private constructor() : Application.ActivityLifecycleCallbacks {
    private var activityStack: Stack<Activity> = Stack()
    private lateinit var application: Application
    private var foregroundCount = 0
    private var foregroundListener: OnAppStatusChangedListener? = null
    companion object {
        fun getInstance() = Holder.INSTANCE
    }

    private object Holder {
        val INSTANCE = AppManager()
    }

    fun init(application: Application) {
        this.application = application
        application.registerActivityLifecycleCallbacks(this)
    }

    fun getApp() : Application {
        return this.application
    }

    fun setForegroundListener(listener: OnAppStatusChangedListener) {
        this.foregroundListener = listener
    }

    override fun onActivityPaused(activity: Activity?) {

    }

    override fun onActivityResumed(activity: Activity?) {
        foregroundCount++
        if (foregroundCount > 0) {
            this.foregroundListener?.onForeground()
        }
    }

    fun getTopActivityOrApp() : Context{
        return if (isAppForeground()) {
            val topActivity = getTopActivity()
            topActivity ?: getApp()
        } else {
            getApp()
        }
    }

    override fun onActivityStarted(activity: Activity?) {

    }

    override fun onActivityDestroyed(activity: Activity?) {
        removeActivity(activity)
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {

    }

    override fun onActivityStopped(activity: Activity?) {
        if (foregroundCount > 0) {
            foregroundCount--
        } else {
            this.foregroundListener?.onBackground()
        }
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        addActivity(activity)
    }

    private fun addActivity(activity: Activity?) {
        activity?.let {
            activityStack.add(it)
        }
    }

    private fun removeActivity(activity: Activity?) {
        if (activityStack.contains(activity)) {
            activityStack.remove(activity)
        }
    }

    fun isAppForeground() : Boolean {
        if (foregroundCount > 0) {
            return true
        }
        return false
    }

    fun getActivity(clazz: Class<Any>) : Activity?{
        activityStack.forEach {
            if (it.javaClass == clazz) {
                return it
            }
        }
        return null
    }

    fun getTopActivity() : Activity? {
        return if (activityStack.isNotEmpty()) {
            activityStack.lastElement()
        } else {
            null
        }
    }

    fun finishTopActivity() {
        val activity = activityStack.lastElement()
        if (activity != null) {
            activityStack.remove(activity)
            activity.finish()
        }
    }

    fun getActivityList(): MutableList<Activity> {
        return activityStack.toMutableList()
    }

    fun finishAllActivity() {
        activityStack.forEach {
            val activity = activityStack.pop()
            activity.finish()
        }
        activityStack.clear()
    }

    fun exitApp() {
        try {
            finishAllActivity()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    interface OnAppStatusChangedListener {
        fun onForeground()
        fun onBackground()
    }

    interface OnActivityDestroyedListener {
        fun onActivityDestroyed(activity: Activity)
    }
}