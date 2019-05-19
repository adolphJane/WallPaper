package com.magicalrice.adolph.wallpaper.widget

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.cardview.widget.CardView
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.magicalrice.adolph.wallpaper.utils.ScreenUtils

class WallpaperCollectionView : CardView {
    private lateinit var img: ImageView
    private lateinit var overlay: View
    private var type: Int = 1

    private val homePadding = ScreenUtils.dp2px(context, 15f)

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context,attrs)
    }

    private fun init(context: Context,attrs: AttributeSet?) {
        img = ImageView(context)
        img.scaleType = ImageView.ScaleType.CENTER_CROP
        val imgParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        addView(img, imgParams)

        radius = ScreenUtils.dp2px(context,5f).toFloat()

        overlay = View(context)
        val overlayParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        addView(overlay,overlayParams)
    }

    fun setType(type: Int) {
        this.type = type
    }

    fun setData(imgPath: String?) {
        post {
            GlideApp.with(this)
                .asBitmap()
                .centerCrop()
                .load(imgPath)
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        val width = ScreenUtils.getScreenWidth(context) - 2 * homePadding
                        val height = width * resource.height / resource.width
                        val params = layoutParams
                        params.width = width
                        params.height = height
                        layoutParams = params

                        val params2 = img.layoutParams
                        params2.width = width
                        params2.height = height
                        img.layoutParams = params2
                        img.setImageBitmap(resource)

                        val params3 = overlay.layoutParams
                        params3.width = width
                        params3.height = height
                        overlay.layoutParams = params3
                    }

                })
        }
    }

    fun setOverlayColor(@ColorInt color: Int) {
        overlay.setBackgroundColor(color)
    }
}