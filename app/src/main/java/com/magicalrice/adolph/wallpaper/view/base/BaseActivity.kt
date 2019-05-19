package com.magicalrice.adolph.wallpaper.view.base

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.magicalrice.adolph.wallpaper.utils.PermissionUtils
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by Adolph on 2018/7/18.
 */
abstract class BaseActivity : AppCompatActivity() {
    private lateinit var dataBinding: ViewDataBinding
    private var mLastClickTime: Long = 0
    private val CLICK_TIME = 500
    protected val mDisposable = CompositeDisposable()
    private lateinit var listener: PermissionUtils.PermissionRequestListener

    @get:LayoutRes
    protected abstract val layoutId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, layoutId)
        onInit(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mDisposable.clear()
    }

    protected abstract fun onInit(savedInstanceState: Bundle?)

    /**
     * 获取ViewDataBinding
     */
    @Suppress("UNCHECKED_CAST")
    protected fun <T : ViewDataBinding> getDataBinding(): T {
        return dataBinding as T
    }

    /**
     * 检测双击
     */
    protected fun vertifyClickTime(): Boolean {
        if (System.currentTimeMillis() - mLastClickTime <= CLICK_TIME) {
            return false
        }
        mLastClickTime = System.currentTimeMillis()
        return false
    }

    fun hideInputMethod() {
        val view = window.decorView
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    fun setListener(listener: PermissionUtils.PermissionRequestListener) {
        this.listener = listener
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PermissionUtils.getInstance().getRequestCode()) {
            var isAllGranted = true
            for (result in grantResults) {
                if (result == PackageManager.PERMISSION_DENIED) {
                    isAllGranted = false
                    break
                }
            }

            if (isAllGranted) {
                listener.requestPermissionsSuccess()
            } else {
                listener.requestPermissionsFail()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}