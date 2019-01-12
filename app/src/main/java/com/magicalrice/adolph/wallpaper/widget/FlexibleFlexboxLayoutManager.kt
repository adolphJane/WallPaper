package com.magicalrice.adolph.wallpaper.widget

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.ViewGroup
import com.google.android.flexbox.FlexboxLayoutManager


class FlexibleFlexboxLayoutManager : FlexboxLayoutManager {
    constructor(context: Context?) : super(context)

    constructor(context: Context?, flexDirection: Int) : super(context, flexDirection)

    constructor(context: Context?, flexDirection: Int, flexWrap: Int) : super(
        context,
        flexDirection,
        flexWrap
    )

    constructor(
        context: Context?,
        attrs: AttributeSet,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun generateLayoutParams(lp: ViewGroup.LayoutParams): RecyclerView.LayoutParams {
        return when (lp) {
            is RecyclerView.LayoutParams -> FlexboxLayoutManager.LayoutParams(lp)
            is ViewGroup.MarginLayoutParams -> FlexboxLayoutManager.LayoutParams(lp)
            else -> FlexboxLayoutManager.LayoutParams(lp)
        }
    }
}