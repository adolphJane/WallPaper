package com.magicalrice.adolph.wallpaper.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.widget.LinearLayout
import com.airbnb.lottie.LottieAnimationView
import com.magicalrice.adolph.wallpaper.R
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.api.RefreshKernel
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.constant.RefreshState
import com.scwang.smartrefresh.layout.constant.SpinnerStyle

class RefreshLottieHeader(context: Context) : LinearLayout(context), RefreshHeader {
    private lateinit var mAnimationView: LottieAnimationView

    init {
        initView(context)
    }

    override fun getView(): View {
        return this
    }

    override fun getSpinnerStyle(): SpinnerStyle {
        return SpinnerStyle.FixedBehind
    }

    override fun setPrimaryColors(vararg colors: Int) {

    }

    override fun onInitialized(kernel: RefreshKernel, height: Int, extendHeight: Int) {

    }

    override fun onMoving(
        isDragging: Boolean,
        percent: Float,
        offset: Int,
        height: Int,
        maxDragHeight: Int
    ) {
    }

    override fun onReleased(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {

    }

    override fun onHorizontalDrag(percentX: Float, offsetX: Int, offsetMax: Int) {

    }

    override fun onStartAnimator(layout: RefreshLayout, height: Int, extendHeight: Int) {
        mAnimationView.playAnimation()
    }

    override fun onFinish(layout: RefreshLayout, success: Boolean): Int {
        mAnimationView.cancelAnimation()
        return 0
    }

    override fun isSupportHorizontalDrag(): Boolean {
        return false
    }

    override fun onStateChanged(
        refreshLayout: RefreshLayout,
        oldState: RefreshState,
        newState: RefreshState
    ) {

    }

    private fun initView(context: Context) {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater.inflate(R.layout.layout_loading_lottie,this)
        mAnimationView = view.findViewById<View>(R.id.loading_lottie) as LottieAnimationView
    }

    fun setAnimationViewJson(animName: String) {
        mAnimationView.setAnimation(animName)
    }

    fun setAnimationViewJson(anim: Animation) {
        mAnimationView.animation = anim
    }
}