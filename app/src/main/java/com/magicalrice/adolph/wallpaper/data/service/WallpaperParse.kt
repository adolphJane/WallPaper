package com.magicalrice.adolph.wallpaper.data.service

import com.magicalrice.adolph.wallpaper.WallpaperApplication
import com.magicalrice.adolph.wallpaper.bean.GalleryImageBean
import com.magicalrice.adolph.wallpaper.bean.WallpaperBean
import com.magicalrice.adolph.wallpaper.utils.ToastUtils
import com.orhanobut.logger.Logger
import org.jsoup.Jsoup

object WallpaperParse {

    fun getDesktopWallpaper(html: String): ArrayList<WallpaperBean>? {
        return try {
            val beanList = arrayListOf<WallpaperBean>()
            val parse = Jsoup.parse(html)
            val documents = parse.select("div.Left_bar div.tab_box ul.clearfix li")
            for (dom in documents) {
                val bean = WallpaperBean()
                bean.href = dom.selectFirst("a[href]").attr("href")
                bean.imgPath = dom.selectFirst("img[src]").attr("data-original")
                bean.title = dom.text()
                beanList.add(bean)
            }
            beanList
        } catch (e: IllegalArgumentException) {
            ToastUtils.showToast(WallpaperApplication.instance,"加载失败")
            null
        }
    }

    fun getNumWallpaper(html: String): Int {
        return try {
            val parse = Jsoup.parse(html)
            val documents = parse.select("div.ptitle em")
            val num = documents.first().text().toIntOrNull()
            num ?: 0
        } catch (e: IllegalArgumentException) {
            ToastUtils.showToast(WallpaperApplication.instance,"加载失败")
            0
        }
    }

    fun getWallpaperBig(html: String): GalleryImageBean? {
        return try {
            val parse = Jsoup.parse(html)
            val imgPath = parse.select("div.pic-meinv a img.pic-large").first().attr("src")
            val size = parse.select("span.size em").text()
            var position = size.indexOf(120.toChar())
            if (position == -1) {
                position = size.indexOf('×')
            }
            val width = size.substring(0,position).toIntOrNull() ?: 0
            val height = size.substring(position + 1,size.length).toIntOrNull() ?: 0
            val bean = GalleryImageBean(imgPath,width,height)
            bean
        } catch (e: IllegalArgumentException) {
            ToastUtils.showToast(WallpaperApplication.instance,"加载失败")
            null
        }
    }
}