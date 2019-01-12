package com.magicalrice.adolph.wallpaper.data

import com.magicalrice.adolph.wallpaper.data.service.WallpaperService


class WPApi {
    private var retrofit = HttpUtils.getRetrofit(HttpConstants.BASE_URL)

    companion object {
        fun getInstance() = Holder.INSTANCE
    }

    private object Holder {
        val INSTANCE = WPApi()
    }

    fun wallpaperApi() : WallpaperService {
        return retrofit.create(WallpaperService::class.java)
    }
}