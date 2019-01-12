package com.magicalrice.adolph.wallpaper.view.collection

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AlertDialog
import com.magicalrice.adolph.wallpaper.bean.GalleryImageBean
import com.magicalrice.adolph.wallpaper.data.NetSubscriber
import com.magicalrice.adolph.wallpaper.data.RetrofitErrorListener
import com.magicalrice.adolph.wallpaper.data.RetrofitNextListener
import com.magicalrice.adolph.wallpaper.data.WPApi
import com.magicalrice.adolph.wallpaper.data.service.WallpaperParse
import com.magicalrice.adolph.wallpaper.utils.DialogUtils
import com.magicalrice.adolph.wallpaper.utils.RxUtils
import com.magicalrice.adolph.wallpaper.view.base.BaseViewModel
import com.trello.rxlifecycle2.android.lifecycle.kotlin.bindToLifecycle
import io.reactivex.Observable
import io.reactivex.functions.Function
import okhttp3.ResponseBody

class WallpaperCollectionViewModel(application: Application) : BaseViewModel(application) {
    private val api = WPApi.getInstance().wallpaperApi()

    val wallpaperList = MutableLiveData<ArrayList<GalleryImageBean>>()

    fun getWallpaperCollectionNum(href: String, activity: FragmentActivity) {
        val dialog: AlertDialog = DialogUtils.showLoadingDialog(activity)
        api.getWallpaperDetail(href)
            .map {
                return@map WallpaperParse.getNumWallpaper(it.string())
            }
            .flatMap {
                val hrefList = arrayListOf<Observable<ResponseBody>>()
                for (i in 1..it) {
                    var html = ""
                    html = if (i == 1) {
                        href
                    } else {
                        href.replace(".html", "_$i.html")
                    }
                    hrefList.add(api.getWallpaperDetail(html))
                }
                return@flatMap Observable.zip(hrefList, Function<Array<Any>, ArrayList<GalleryImageBean>> {
                    val imgList = arrayListOf<GalleryImageBean>()
                    for (code in it) {
                        if (code is ResponseBody) {
                            WallpaperParse.getWallpaperBig(code.string())?.let {bean ->
                                imgList.add(bean)
                            }
                        }
                    }
                    return@Function imgList
                })
            }
            .compose(RxUtils.io_main())
            .bindToLifecycle(activity)
            .subscribe(NetSubscriber(dialog,object : RetrofitNextListener<ArrayList<GalleryImageBean>> {
                override fun onNext(t: ArrayList<GalleryImageBean>) {
                    wallpaperList.value = t
                }
            }, getApplication()))
    }
}