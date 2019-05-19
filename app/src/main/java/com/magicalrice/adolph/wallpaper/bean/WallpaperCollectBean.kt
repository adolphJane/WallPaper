package com.magicalrice.adolph.wallpaper.bean

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "collect")
data class WallpaperCollectBean(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var imgPath: String
)