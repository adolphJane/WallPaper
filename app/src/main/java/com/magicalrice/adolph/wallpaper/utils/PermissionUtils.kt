package com.magicalrice.adolph.wallpaper.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import com.magicalrice.adolph.wallpaper.view.base.BaseActivity

class PermissionUtils private constructor(){
    private var requestCode:Int = 0

    companion object {
        fun getInstance() = Holder.instance
    }

    private object Holder {
        val instance = PermissionUtils()
    }

    fun checkPermission(activity: FragmentActivity,permission:String) : Boolean{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(activity,permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    fun requestPermission(activity: FragmentActivity,permissions: Array<String>,requestCode: Int,listener: PermissionRequestListener) {
        this.requestCode = requestCode
        var deniedPermissionList = getDeniedPermissions(activity,permissions)
        if (activity != null) {
            (activity as BaseActivity).setListener(listener)
            if (deniedPermissionList != null && deniedPermissionList.size > 0) {

                if (shouldShowRationable(activity,deniedPermissionList)) {
                    DialogUtils.showDialog(activity, "温馨提示", "当前应用缺少权限，请点击\"设置\"-\"应用权限\"-打开所需权限", "", "确定",null,null)
                } else {
                    ActivityCompat.requestPermissions(activity, permissions,requestCode)
                }
            } else {
                listener.requestPermissionsSuccess()
            }
        }
    }

    fun shouldShowRationable(activity: FragmentActivity,permissions: ArrayList<String>) : Boolean{
        var shouldShowPermissionList = arrayListOf<String>()
        for (permission in permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,permission)) {
                shouldShowPermissionList.add(permission)
            }
        }

        if (shouldShowPermissionList.size == permissions.size) {
            return true
        } else {
            return false
        }
    }

    fun getDeniedPermissions(context: Context,permissions: Array<String>) : ArrayList<String>?{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var deniedPermissionList = arrayListOf<String>()
            for (permission in permissions) {
                if (context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    deniedPermissionList.add(permission)
                }
            }

            var size = deniedPermissionList.size
            if (size > 0) {
                return deniedPermissionList
            }
        }
        return null
    }

    fun getRequestCode() : Int{
        return requestCode
    }

    interface PermissionRequestListener {
        fun requestPermissionsSuccess()
        fun requestPermissionsFail()
    }
}