package com.magicalrice.adolph.wallpaper.utils

import android.content.Context
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.magicalrice.adolph.wallpaper.R


object DialogUtils {
    fun showDialog(context: Context, title: String, content: String, cancelText: String, sureText: String, cancelListener: View.OnClickListener?, confirmListener: View.OnClickListener?) {
        val builder = AlertDialog.Builder(context)
        val view = LayoutInflater.from(context).inflate(R.layout.layout_dialog_custom,null)
        val dialog = builder.create()
        dialog.show()
        val params = dialog.window?.attributes
        params?.width = ScreenUtils.dp2px(context,300f)
        params?.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window?.attributes = params
        if (TextUtils.isEmpty(title)) {
            view.findViewById<TextView>(R.id.dialog_title).visibility = View.INVISIBLE
            view.findViewById<TextView>(R.id.dialog_title).text = ""
        } else {
            view.findViewById<TextView>(R.id.dialog_title).text = title
        }

        if (TextUtils.isEmpty(content)) {
            view.findViewById<TextView>(R.id.dialog_content).visibility = View.GONE
            view.findViewById<TextView>(R.id.dialog_content).text = ""
        } else {
            view.findViewById<TextView>(R.id.dialog_content).text = content
        }

        if (TextUtils.isEmpty(cancelText)) {
            view.findViewById<TextView>(R.id.dialog_btn_cancel).text = ""
            view.findViewById<TextView>(R.id.dialog_btn_cancel).visibility = View.GONE
        } else {
            view.findViewById<TextView>(R.id.dialog_btn_cancel).text = cancelText
        }

        if (TextUtils.isEmpty(sureText)) {
            view.findViewById<TextView>(R.id.dialog_btn_sure).text = ""
        } else {
            view.findViewById<TextView>(R.id.dialog_btn_sure).text = sureText
        }

        if (cancelListener == null) {
            view.findViewById<TextView>(R.id.dialog_btn_cancel).setOnClickListener {
                dialog.dismiss()
            }
        } else {
            view.findViewById<TextView>(R.id.dialog_btn_cancel).setOnClickListener{
                cancelListener.onClick(it)
                dialog.dismiss()
            }
        }

        if (confirmListener == null) {
            view.findViewById<TextView>(R.id.dialog_btn_sure).setOnClickListener {
                dialog.dismiss()
            }
        } else {
            view.findViewById<TextView>(R.id.dialog_btn_sure).setOnClickListener{
                confirmListener.onClick(it)
                dialog.dismiss()
            }
        }
        dialog.window?.setContentView(view)
        dialog.window?.setGravity(Gravity.CENTER)
    }

    fun showLoadingDialog(context: Context) : AlertDialog {
        val builder = AlertDialog.Builder(context,R.style.LoadingDialog)
        val view = LayoutInflater.from(context).inflate(R.layout.layout_dialog_loading,null)
        val dialog = builder.create()
        dialog.show()
        val params = dialog.window?.attributes
        params?.width = ScreenUtils.dp2px(context,82f)
        params?.height = ScreenUtils.dp2px(context,82f)
        dialog.window?.attributes = params
        dialog.window?.setContentView(view)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.window?.setGravity(Gravity.CENTER)
        val lottie = view.findViewById<LottieAnimationView>(R.id.img_loading)
        lottie.playAnimation()
        dialog.setOnDismissListener {
            lottie.pauseAnimation()
        }
        return dialog
    }
}