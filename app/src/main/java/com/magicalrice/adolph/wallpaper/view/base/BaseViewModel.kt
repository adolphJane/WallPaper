package com.magicalrice.adolph.wallpaper.view.base

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.support.v4.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.magicalrice.adolph.wallpaper.data.NetSubscriber
import com.magicalrice.adolph.wallpaper.data.RetrofitErrorListener
import com.magicalrice.adolph.wallpaper.utils.ToastUtils
import io.reactivex.Observable

open class BaseViewModel(application: Application) : AndroidViewModel(application) {

    fun showToast(content: String?) {
        if (content?.isEmpty() == false) {
            ToastUtils.showShort(content)
        }
    }
}