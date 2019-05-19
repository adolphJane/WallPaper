package com.magicalrice.adolph.wallpaper.view.main

import android.app.Application
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.magicalrice.adolph.wallpaper.bean.WallpaperBean
import com.magicalrice.adolph.wallpaper.bean.WallpaperSelectBean
import com.magicalrice.adolph.wallpaper.data.NetSubscriber
import com.magicalrice.adolph.wallpaper.data.RetrofitErrorListener
import com.magicalrice.adolph.wallpaper.data.WPApi
import com.magicalrice.adolph.wallpaper.data.service.WallpaperParse
import com.magicalrice.adolph.wallpaper.utils.RxUtils
import com.magicalrice.adolph.wallpaper.view.base.BaseViewModel
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.trello.rxlifecycle2.android.lifecycle.kotlin.bindToLifecycle
import io.reactivex.Observable

class MainViewModule(application: Application) : BaseViewModel(application) {
    private val api = WPApi.getInstance().wallpaperApi()
    private val totalPage = 5
    private var pageNo: Int = 1
    val desktopSelect = MutableLiveData<WallpaperSelectBean>()
    val phoneSelect = MutableLiveData<WallpaperSelectBean>()

    init {
        desktopSelect.value = WallpaperSelectBean(0,0,0)
        phoneSelect.value = WallpaperSelectBean(0,0,0)
    }

    fun getWallpaper(activity: FragmentActivity, wallpaperType: Int, isRefresh: Boolean)  : Observable<ArrayList<WallpaperBean>?>? {
        if (isRefresh) {
            pageNo = 1
        } else {
            pageNo++
        }

        return when(wallpaperType) {
            1 -> {
                getDesktopWallpaper(activity)
            }
            2 -> {
                getPhoneWallpaper(activity)
            }
            else -> {
                getDesktopWallpaper(activity)
            }
        }
    }

    private fun getDesktopWallpaper(activity: FragmentActivity) : Observable<ArrayList<WallpaperBean>?>? {
        desktopSelect.value?.run {
            return api.getWallpaperDesktop(style, color, size, pageNo)
                .map {
                    return@map WallpaperParse.getDesktopWallpaper(it.string())
                }.compose(RxUtils.io_main())
                .bindToLifecycle(activity)
        }
        return null
    }

    private fun getPhoneWallpaper(activity: FragmentActivity)  : Observable<ArrayList<WallpaperBean>?>? {
        phoneSelect.value?.run {
            return api.getWallpaperPhone(style, color, size, pageNo)
                .map {
                    return@map WallpaperParse.getDesktopWallpaper(it.string())
                }.compose(RxUtils.io_main())
                .bindToLifecycle(activity)
        }
        return null
    }

    fun <T, A> refreshData(
        observable: Observable<ArrayList<T>?>?,
        adapter: A,
        refreshLayout: SmartRefreshLayout?,
        isRefresh: Boolean
    ) {
        adapter as BaseQuickAdapter<T, BaseViewHolder>
        observable?.subscribe(NetSubscriber(refreshLayout,object :
            RetrofitErrorListener<ArrayList<T>?> {
            override fun onError(t: Throwable) {
                adapter.loadMoreFail()
            }

            override fun onNext(t: ArrayList<T>?) {
                t?.let {
                    if (isRefresh) {
                        adapter.setNewData(t)
                        if (pageNo >= totalPage) {
                            adapter.loadMoreEnd()
                        }
                    } else {
                        adapter.addData(t)
                        if (pageNo >= totalPage) {
                            adapter.loadMoreEnd()
                        } else {
                            adapter.loadMoreComplete()
                        }
                    }
                }
            }
        }, getApplication()))
    }

    fun setData(data: Int, type: Int, wallpaperType: Int) {
        if (wallpaperType == 1) {
            val bean = desktopSelect.value
            when (type) {
                1 -> bean?.color = data
                2 -> bean?.size = data
                3 -> bean?.style = data
            }
            desktopSelect.value = bean
        } else if (wallpaperType == 2) {
            val bean = phoneSelect.value
            when (type) {
                1 -> bean?.color = data
                2 -> bean?.size = data
                3 -> bean?.style = data
            }
            phoneSelect.value = bean
        }
    }
}