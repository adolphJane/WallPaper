package com.magicalrice.adolph.wallpaper.data.local

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.magicalrice.adolph.wallpaper.bean.WallpaperCollectBean

@Dao
interface WallpaperListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWallpaper(wallpaperBean: WallpaperCollectBean)

    @Query("SELECT * FROM collect WHERE imgPath == (:imgUrl)")
    fun findWallpaper(imgUrl: String) : LiveData<List<WallpaperCollectBean>>

    @Delete
    fun deleteWallpaer(wallpaperBean: WallpaperCollectBean)
}