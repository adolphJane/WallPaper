package com.magicalrice.adolph.wallpaper.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.magicalrice.adolph.wallpaper.bean.WallpaperCollectBean

@Database(entities = [WallpaperCollectBean::class], version = 1, exportSchema = false)
abstract class WallpaperDatabase : RoomDatabase() {
    abstract fun wallpaperListDao() : WallpaperListDao
}