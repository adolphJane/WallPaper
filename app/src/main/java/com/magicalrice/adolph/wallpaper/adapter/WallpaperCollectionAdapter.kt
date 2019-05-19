package com.magicalrice.adolph.wallpaper.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.Nullable
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.magicalrice.adolph.wallpaper.R
import com.magicalrice.adolph.wallpaper.widget.WallpaperCollectionView

class WallpaperCollectionAdapter(@Nullable data: List<String>, type: Int) : BaseQuickAdapter<String,BaseViewHolder>(data) {
    private var type: Int = 1
    init {
        this.type = type
    }
    override fun createBaseViewHolder(parent: ViewGroup?, layoutResId: Int): BaseViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.layout_wallpaper_collection,parent,false)
        val collection = view.findViewById<WallpaperCollectionView>(R.id.wallpaper)
        collection.setType(type)
        return createBaseViewHolder(view)
    }

    override fun convert(helper: BaseViewHolder?, item: String?) {
        helper?.let {
            it.getView<WallpaperCollectionView>(R.id.wallpaper).setData(item)
        }
    }
}