package com.magicalrice.adolph.wallpaper.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.github.clans.fab.FloatingActionMenu
import com.google.android.material.snackbar.Snackbar


class FloatingActionBehavior : CoordinatorLayout.Behavior<FloatingActionMenu> {
    constructor() : super()
    constructor(context: Context, attrs: AttributeSet) : super(context,attrs)

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: FloatingActionMenu,
        dependency: View
    ): Boolean {
        return dependency is Snackbar.SnackbarLayout
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: FloatingActionMenu,
        dependency: View
    ): Boolean {
        val fTransY = getFabTranslationYForSnackbar(parent, child)

//        val iRate = -fTransY / dependency.height

//        child.rotation = iRate * 90

        child.translationY = fTransY

        return false
    }

    /**
     * 用来获取SnackBar逐渐弹出来的时候变化的高度
     *
     * @param parent
     * @param fab
     * @return
     */
    private fun getFabTranslationYForSnackbar(
        parent: CoordinatorLayout,
        fab: FloatingActionMenu
    ): Float {
        var minOffset = 0f
        val dependencies = parent.getDependencies(fab)
        var i = 0
        val z = dependencies.size
        while (i < z) {
            val view = dependencies[i]
            if (view is Snackbar.SnackbarLayout && parent.doViewsOverlap(fab, view)) {
                minOffset = Math.min(
                    minOffset,
                    ViewCompat.getTranslationY(view) - view.getHeight()
                )
            }
            i++
        }
        return minOffset
    }
}