package com.magicalrice.project.base_libs.utils

import android.support.v4.app.FragmentActivity
import java.lang.Exception
import java.util.*

class AppManager private constructor() {
    private var activityStack: Stack<FragmentActivity>

    init {
        activityStack = Stack()
    }

    companion object {
        fun getInstance() = Holder.INSTANCE
    }

    private object Holder {
        val INSTANCE = AppManager()
    }

    fun addActivity(activity: FragmentActivity) {
        if (activityStack == null) {
            activityStack = Stack()
        }
        activityStack.add(activity)
    }

    fun removeActivity(activity: FragmentActivity) {
        if (activityStack != null && activityStack.contains(activity)) {
            activityStack.remove(activity)
        }
    }

    fun getActivity(clazz: Class<Any>) : FragmentActivity?{
        activityStack.forEach {
            if (it.javaClass.equals(clazz)) {
                return it
            }
        }
        return null
    }

    fun getTopActivity() : FragmentActivity{
        return activityStack.lastElement()
    }

    fun finishTopActivity() {
        val activity = activityStack.lastElement()
        if (activity != null) {
            activityStack.remove(activity)
            activity.finish()
        }
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
            android.os.Process.killProcess(android.os.Process.myPid())
            System.exit(0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}