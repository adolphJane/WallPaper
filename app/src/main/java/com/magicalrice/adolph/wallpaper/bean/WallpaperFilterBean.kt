package com.magicalrice.adolph.wallpaper.bean

data class WallpaperFilterBean(
        var id: Int = 0,
        var content: String = "",
        var type: Int = 0,
        var color: String = "",
        var theme: Int = 0,
        var isSelect: Boolean = false
)