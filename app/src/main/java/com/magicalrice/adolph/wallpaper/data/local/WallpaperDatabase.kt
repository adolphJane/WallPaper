package com.magicalrice.adolph.wallpaper.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.magicalrice.adolph.wallpaper.bean.WallpaperCollectBean

@Database(entities = [WallpaperCollectBean::class], version = 1)
abstract class WallpaperDatabase : RoomDatabase() {
    abstract fun wallpaperListDao() : WallpaperListDao
}