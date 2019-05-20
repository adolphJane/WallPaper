package com.magicalrice.adolph.wallpaper.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.magicalrice.adolph.wallpaper.bean.WallpaperCollectBean

@Dao
interface WallpaperListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWallpaper(wallpaperBean: WallpaperCollectBean)

    @Query("SELECT * FROM collect WHERE imgPath == (:imgUrl)")
    fun findWallpaper(imgUrl: String) : LiveData<List<WallpaperCollectBean>>

    @Query("SELECT * FROM collect WHERE type == (:type)")
    fun loadAllWallpaper(type: Int) : LiveData<List<WallpaperCollectBean>>

    @Delete
    fun deleteWallpaer(wallpaperBean: WallpaperCollectBean)
}