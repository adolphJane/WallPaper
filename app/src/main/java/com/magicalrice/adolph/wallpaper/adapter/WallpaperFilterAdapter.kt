package com.magicalrice.adolph.wallpaper.adapter

import android.support.annotation.LayoutRes
import android.support.annotation.Nullable
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.magicalrice.adolph.wallpaper.R
import com.magicalrice.adolph.wallpaper.bean.WallpaperFilterBean

class WallpaperFilterAdapter(@LayoutRes layoutId: Int, @Nullable data: List<WallpaperFilterBean>) : BaseQuickAdapter<WallpaperFilterBean, BaseViewHolder>(layoutId, data) {

    override fun convert(helper: BaseViewHolder?, item: WallpaperFilterBean?) {
        val textView = helper?.getView<TextView>(R.id.filter_content)
        val view = helper?.getView<TextView>(R.id.view_color)
        if (item?.theme == 0) {
            textView?.text = item.content
            textView?.visibility = View.VISIBLE
            view?.visibility = View.GONE
            if (item?.isSelect) {
                textView?.setTextColor(ContextCompat.getColor(mContext,R.color.yellow1))
            } else {
                textView?.setTextColor(ContextCompat.getColor(mContext,R.color.white1))
            }
        } else if (item?.theme == 1) {
            textView?.visibility = View.GONE
            view?.visibility = View.VISIBLE
            when(item?.color) {
                "yellow" -> view?.background = ContextCompat.getDrawable(mContext,R.drawable.selector_yellow_background)
                "orange" -> view?.background = ContextCompat.getDrawable(mContext,R.drawable.selector_orange_background)
                "red" -> view?.background = ContextCompat.getDrawable(mContext,R.drawable.selector_red_background)
                "pink" -> view?.background = ContextCompat.getDrawable(mContext,R.drawable.selector_pink_background)
                "purple" -> view?.background = ContextCompat.getDrawable(mContext,R.drawable.selector_purple_background)
                "green" -> view?.background = ContextCompat.getDrawable(mContext,R.drawable.selector_green_background)
                "blue" -> view?.background = ContextCompat.getDrawable(mContext,R.drawable.selector_blue_background)
                "gray" -> view?.background = ContextCompat.getDrawable(mContext,R.drawable.selector_gray_background)
                "black" -> view?.background = ContextCompat.getDrawable(mContext,R.drawable.selector_black_background)
                "colorful" -> view?.background = ContextCompat.getDrawable(mContext,R.drawable.selector_colorful_background)
                "white" -> view?.background = ContextCompat.getDrawable(mContext,R.drawable.selector_white_background)
            }
            view?.isSelected = item?.isSelect
        }

        helper?.addOnClickListener(R.id.filter_content)
        helper?.addOnClickListener(R.id.view_color)
    }

    fun clearData(position: Int) {
        data.forEach {
            it.isSelect = false
        }
        data[position].isSelect = true
        notifyDataSetChanged()
    }
}