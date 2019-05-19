package com.magicalrice.adolph.wallpaper.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.magicalrice.adolph.wallpaper.utils.ScreenUtils
import com.orhanobut.logger.Logger

class ToolBarViewBehavior : CoordinatorLayout.Behavior<View> {

    constructor() : super()

    constructor(context: Context,attrs: AttributeSet) : super(context, attrs)

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL || super.onStartNestedScroll(
            coordinatorLayout,
            child,
            directTargetChild,
            target,
            axes,
            type
        )
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout, child: View, target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type)

            Logger.e("scrollY: $dyConsumed")
            val height = ScreenUtils.dp2px(coordinatorLayout.context,70f)
        if (dyConsumed > 0) {
            if (child.bottom <= 0) {
                child.top = 0
            } else {
                ViewCompat.offsetTopAndBottom(child,-dyConsumed)
            }
        } else if (dyConsumed < 0){
            if (child.bottom >= height) {
                child.top = height
            } else {
                ViewCompat.offsetTopAndBottom(child,-dyConsumed)
            }
        }
    }
}