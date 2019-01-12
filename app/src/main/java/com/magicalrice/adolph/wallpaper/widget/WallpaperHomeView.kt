package com.magicalrice.adolph.wallpaper.widget

import android.content.Context
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.RelativeLayout.LayoutParams
import android.widget.TextView
import com.magicalrice.adolph.wallpaper.R
import com.magicalrice.adolph.wallpaper.bean.WallpaperBean
import com.magicalrice.adolph.wallpaper.utils.ScreenUtils

class WallpaperHomeView : RelativeLayout {
    private lateinit var img: ImageView
    private lateinit var textView: TextView
    private var type: Int = 1

    private val textHeight = ScreenUtils.dp2px(context,40f)
    private val homePadding = ScreenUtils.dp2px(context, 15f)

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context,attrs)
    }

    private fun init(context: Context?,attrs: AttributeSet?) {
        img = ImageView(context)
        img.scaleType = ImageView.ScaleType.CENTER_CROP
        val imgParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        imgParams.addRule(RelativeLayout.CENTER_HORIZONTAL)
        addView(img, imgParams)

        textView = TextView(context)
        val txtParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        txtParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        txtParams.addRule(RelativeLayout.CENTER_HORIZONTAL)
        textView.gravity = Gravity.CENTER_VERTICAL
        textView.setTextColor(ContextCompat.getColor(context!!,R.color.black))
        textView.maxLines = 1
        textView.ellipsize = TextUtils.TruncateAt.END
        addView(textView, txtParams)

        val a = context.obtainStyledAttributes(attrs, R.styleable.WallpaperHomeView)
        type = a?.getInteger(R.styleable.WallpaperHomeView_type,1) ?: 1
        a.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val screenWidth = ScreenUtils.getScreenWidth(context)
        var width:Int
        var height:Int
        var imgHeight:Int
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
        height = imgHeight + textHeight
        setMeasuredDimension(width, height)
        img.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(imgHeight, MeasureSpec.EXACTLY))
        textView.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(textHeight, MeasureSpec.EXACTLY))

    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        textView.layout(0,measuredHeight - ScreenUtils.dp2px(context,40f),measuredWidth,measuredHeight)
    }

    fun setData(data: WallpaperBean?) {
        post {
            GlideApp.with(this)
                    .load(data?.imgPath)
                    .override(img.measuredWidth,img.measuredHeight)
                    .into(img)
            textView.text = data?.title
        }

    }
}