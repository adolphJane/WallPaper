package com.magicalrice.adolph.wallpaper.adapter

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.Nullable
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.magicalrice.adolph.wallpaper.R
import com.magicalrice.adolph.wallpaper.bean.GalleryImageBean
import com.magicalrice.adolph.wallpaper.utils.ScreenUtils
import com.magicalrice.adolph.wallpaper.widget.GlideApp

class GalleryCollectionAdapter(@LayoutRes layoutId: Int, @Nullable data: List<GalleryImageBean>, wallpaperType: Int) : BaseQuickAdapter<GalleryImageBean,BaseViewHolder>(layoutId, data) {
    private var wallpaperType = wallpaperType

    override fun createBaseViewHolder(parent: ViewGroup?, layoutResId: Int): BaseViewHolder {
        val view = getItemView(layoutResId,parent)
        var itemWith = 0
        var itemHeight = 0
        if (wallpaperType == 1) {
            itemWith = ScreenUtils.getScreenWidth(mContext)
            if (data.size > 0) {
                val (_, width1, height) = data[0]
                val ratio = height.toFloat() / width1
                itemHeight = Math.round(itemWith * ratio)
            }

            val params = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                itemHeight
            )
            view.layoutParams = params
        } else if (wallpaperType == 2) {
            itemWith = ScreenUtils.getScreenWidth(mContext)
            if (data.size > 0) {
                val (_, width1, height) = data[0]
                val ratio = height.toFloat() / width1
                itemHeight = Math.round(itemWith * ratio)
            }

            val params = ViewGroup.LayoutParams(
                itemWith, itemHeight
            )
            view.layoutParams = params
        }

        return createBaseViewHolder(view)
    }

    override fun convert(helper: BaseViewHolder?, item: GalleryImageBean?) {
        helper?.let {
            GlideApp.with(mContext)
                .load(item?.imgSrc)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(it.getView(R.id.image))
            it.addOnClickListener(R.id.img_root)
        }
    }
}