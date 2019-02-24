package com.magicalrice.adolph.wallpaper.bean

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "collect")
data class WallpaperCollectBean(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var imgPath: String
)