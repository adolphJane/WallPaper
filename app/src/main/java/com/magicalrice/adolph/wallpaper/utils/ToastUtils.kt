package com.magicalrice.adolph.wallpaper.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.magicalrice.adolph.wallpaper.utils.toast.IToast
import com.magicalrice.adolph.wallpaper.utils.toast.ToastFactory

@SuppressLint("ShowToast")
object ToastUtils {
    private val COLOR_DEFAULT: Int = 0XFFFFFF
    private val handler: Handler = Handler(Looper.getMainLooper())
    private val NULL = "null"
    private var iToast: IToast? = null
    private var sGravity = -1
    private var sXOffset = -1
    private var sYOffset = -1
    private var sBgColor = COLOR_DEFAULT
    private var sBgResource = -1
    private var sMsgColor = COLOR_DEFAULT
    private var sMsgTextSize = -1

    /**
     * 设置吐司位置
     *
     * @param gravity The gravity.
     * @param xOffset X-axis offset, in pixel.
     * @param yOffset Y-axis offset, in pixel.
     */
    fun setGravity(gravity: Int, xOffset: Int, yOffset: Int) : ToastUtils{
        sGravity = gravity
        sXOffset = xOffset
        sYOffset = yOffset
        return this
    }

    /**
     * 设置背景颜色
     *
     * @param backgroundColor The color of background.
     */
    fun setBgColor(@ColorInt backgroundColor: Int) : ToastUtils{
        sBgColor = backgroundColor
        return this
    }

    /**
     * 设置背景资源
     *
     * @param bgResource The resource of background.
     */
    fun setBgResource(@DrawableRes bgResource: Int) : ToastUtils{
        sBgResource = bgResource
        return this
    }

    /**
     * 设置消息颜色
     *
     * @param msgColor The color of message.
     */
    fun setMsgColor(@ColorInt msgColor: Int) : ToastUtils{
        sMsgColor = msgColor
        return this
    }

    /**
     * 设置消息字体大小
     *
     * @param textSize The text size of message.
     */
    fun setMsgTextSize(textSize: Int) : ToastUtils{
        sMsgTextSize = textSize
        return this
    }

    /**
     * 显示短时吐司
     *
     * @param text The text.
     */
    fun showShort(text: CharSequence?) {
        show(text ?: NULL, Toast.LENGTH_SHORT)
    }

    /**
     * 显示短时吐司
     *
     * @param resId The resource id for text.
     */
    fun showShort(@StringRes resId: Int) {
        show(resId, Toast.LENGTH_SHORT)
    }

    /**
     * 显示短时吐司
     *
     * @param resId The resource id for text.
     * @param args  The args.
     */
    fun showShort(@StringRes resId: Int, vararg args: Any) {
        showMore(resId, Toast.LENGTH_SHORT, *args)
    }

    /**
     * 显示短时吐司
     *
     * @param format The format.
     * @param args   The args.
     */
    fun showShort(format: String, vararg args: Any) {
        showMore(format, Toast.LENGTH_SHORT, *args)
    }

    /**
     * 显示长时吐司
     *
     * @param text The text.
     */
    fun showLong(text: CharSequence?) {
        show(text ?: NULL, Toast.LENGTH_LONG)
    }

    /**
     * 显示长时吐司
     *
     * @param resId The resource id for text.
     */
    fun showLong(@StringRes resId: Int) {
        show(resId, Toast.LENGTH_LONG)
    }

    /**
     * 显示长时吐司
     *
     * @param resId The resource id for text.
     * @param args  The args.
     */
    fun showLong(@StringRes resId: Int, vararg args: Any) {
        showMore(resId, Toast.LENGTH_LONG, *args)
    }

    /**
     * 显示长时吐司
     *
     * @param format The format.
     * @param args   The args.
     */
    fun showLong(format: String, vararg args: Any) {
        showMore(format, Toast.LENGTH_LONG, *args)
    }

    /**
     * 显示短时自定义吐司
     *
     * @param layoutId ID for an XML layout resource to load.
     */
    fun showCustomShort(@LayoutRes layoutId: Int): View {
        val view = getView(layoutId)
        show(view, Toast.LENGTH_SHORT)
        return view
    }

    /**
     * 显示长时自定义吐司
     *
     * @param layoutId ID for an XML layout resource to load.
     */
    fun showCustomLong(@LayoutRes layoutId: Int): View {
        val view = getView(layoutId)
        show(view, Toast.LENGTH_LONG)
        return view
    }

    /**
     * 显示短时自定义吐司
     *
     * @param layoutId ID for an XML layout resource to load.
     */
    fun showCustomShort(view: View): View {
        show(view, Toast.LENGTH_SHORT)
        return view
    }

    /**
     * 显示长时自定义吐司
     *
     * @param layoutId ID for an XML layout resource to load.
     */
    fun showCustomLong(view: View): View {
        show(view, Toast.LENGTH_LONG)
        return view
    }

    /**
     * 取消吐司显示
     */
    fun cancel() {
        iToast?.cancel()
    }

    private fun show(resId: Int, duration: Int) {
        try {
            val text = AppManager.getInstance().getApp().resources.getText(resId)
            show(text, duration)
        } catch (ignore: Exception) {
            show(resId.toString(), duration)
        }

    }

    private fun showMore(resId: Int, duration: Int, vararg args: Any) {
        try {
            val text = AppManager.getInstance().getApp().resources.getText(resId)
            val format = String.format(text.toString(), *args)
            show(format, duration)
        } catch (ignore: Exception) {
            show(resId.toString(), duration)
        }

    }

    private fun showMore(format: String?, duration: Int, vararg args: Any) {
        var text: String?
        if (format == null) {
            text = NULL
        } else {
            text = String.format(format, *args)
            if (text == null) {
                text = NULL
            }
        }
        show(text, duration)
    }

    private fun show(text: CharSequence, duration: Int) {
        handler.post{
            cancel()
            iToast = ToastFactory.makeToast(AppManager.getInstance().getApp(), text, duration)
            val tvMessage = iToast?.getView()?.findViewById<TextView>(android.R.id.message)
            if (sMsgColor != COLOR_DEFAULT) {
                tvMessage?.setTextColor(sMsgColor)
            }
            if (sMsgTextSize != -1) {
                tvMessage?.textSize = sMsgTextSize.toFloat()
            }
            if (sGravity != -1 || sXOffset != -1 || sYOffset != -1) {
                iToast?.setGravity(sGravity, sXOffset, sYOffset)
            }
            setBg(tvMessage)
            iToast?.show()
        }
    }

    private fun show(view: View, duration: Int) {
        handler.post{
            cancel()
            iToast = ToastFactory.newToast(AppManager.getInstance().getApp())
            iToast?.setView(view)
            iToast?.setDuration(duration)
            if (sGravity != -1 || sXOffset != -1 || sYOffset != -1) {
                iToast?.setGravity(sGravity, sXOffset, sYOffset)
            }
            setBg()
            iToast?.show()
        }
    }

    private fun setBg() {
        if (sBgResource != -1) {
            val toastView = iToast?.getView()
            toastView?.setBackgroundResource(sBgResource)
        } else if (sBgColor != COLOR_DEFAULT) {
            val toastView = iToast?.getView()
            val background = toastView?.background
            if (background != null) {
                background.colorFilter = PorterDuffColorFilter(sBgColor, PorterDuff.Mode.SRC_IN)
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    toastView?.background = ColorDrawable(sBgColor)
                } else {
                    toastView?.setBackgroundColor(ContextCompat.getColor(AppManager.getInstance().getApp(),sBgColor))
                }
            }
        }
    }

    private fun setBg(tvMsg: TextView?) {
        if (sBgResource != -1) {
            val toastView = iToast?.getView()
            toastView?.setBackgroundResource(sBgResource)
            tvMsg?.setBackgroundColor(Color.TRANSPARENT)
        } else if (sBgColor != COLOR_DEFAULT) {
            val toastView = iToast?.getView()
            val tvBg = toastView?.background
            val msgBg = tvMsg?.background
            if (tvBg != null && msgBg != null) {
                tvBg.colorFilter = PorterDuffColorFilter(sBgColor, PorterDuff.Mode.SRC_IN)
                tvMsg.setBackgroundColor(Color.TRANSPARENT)
            } else if (tvBg != null) {
                tvBg.colorFilter = PorterDuffColorFilter(sBgColor, PorterDuff.Mode.SRC_IN)
            } else if (msgBg != null) {
                msgBg.colorFilter = PorterDuffColorFilter(sBgColor, PorterDuff.Mode.SRC_IN)
            } else {
                toastView?.setBackgroundColor(sBgColor)
            }
        }
    }

    private fun getView(@LayoutRes layoutId: Int): View {
        val inflate = AppManager.getInstance().getApp().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        return inflate.inflate(layoutId, null)
    }
}