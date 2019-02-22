package com.magicalrice.adolph.wallpaper.widget

import android.content.Context
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.RelativeLayout.LayoutParams
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.magicalrice.adolph.wallpaper.R
import com.magicalrice.adolph.wallpaper.bean.WallpaperBean
import com.magicalrice.adolph.wallpaper.utils.ScreenUtils

class WallpaperHomeView : RelativeLayout {
    private lateinit var img: ImageView
    private var type: Int = 1
    private val colorList = arrayOf(R.color.placeholder1,R.color.placeholder2,R.color.placeholder3,R.color.placeholder4,R.color.placeholder5,R.color.placeholder6,R.color.placeholder7,R.color.placeholder8)

    private val homePadding = ScreenUtils.dp2px(context, 0f)

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context?, attrs: AttributeSet?) {
        img = ImageView(context)
        img.scaleType = ImageView.ScaleType.CENTER_CROP
        val imgParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        imgParams.addRule(RelativeLayout.CENTER_HORIZONTAL)
        addView(img, imgParams)

        val a = context?.obtainStyledAttributes(attrs, R.styleable.WallpaperHomeView)
        type = a?.getInteger(R.styleable.WallpaperHomeView_type, 1) ?: 1
        a?.recycle()

        val i = (0 until 8).random()
        context?.let {
            setBackgroundColor(ContextCompat.getColor(context,colorList[i]))
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val screenWidth = ScreenUtils.getScreenWidth(context)
        var width: Int
        var height: Int
        var imgHeight: Int
        width = if (type == 1) {
            screenWidth
        } else {
            (screenWidth - 3 * homePadding) / 2
        }
        imgHeight = if (type == 1) {
            width * 133 / 200
        } else {
            width * 310 / 200
        }
        height = imgHeight
        setMeasuredDimension(width, height)
        img.measure(
            MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(imgHeight, MeasureSpec.EXACTLY)
        )
    }

    fun setData(data: WallpaperBean?) {
        post {
            GlideApp.with(this)
                .load(data?.imgPath)
                .transition(DrawableTransitionOptions.withCrossFade())
                .override(img.measuredWidth, img.measuredHeight)
                .into(img)
        }

    }
}