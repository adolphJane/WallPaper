package com.magicalrice.adolph.wallpaper.adapter

import androidx.annotation.LayoutRes
import androidx.annotation.Nullable
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.magicalrice.adolph.wallpaper.R
import com.magicalrice.adolph.wallpaper.bean.WallpaperBean
import com.magicalrice.adolph.wallpaper.bean.WallpaperCollectBean
import com.magicalrice.adolph.wallpaper.bean.WallpaperFilterBean
import com.magicalrice.adolph.wallpaper.widget.WallpaperHomeView
import java.io.File

class WallpaperDownloadAdapter(@LayoutRes layoutId: Int, @Nullable data: List<File>) : BaseQuickAdapter<File,BaseViewHolder>(layoutId,data) {
    override fun convert(helper: BaseViewHolder?, item: File?) {
        helper?.getView<WallpaperHomeView>(R.id.homeView)?.setImageUrl(item?.absolutePath)
    }
}