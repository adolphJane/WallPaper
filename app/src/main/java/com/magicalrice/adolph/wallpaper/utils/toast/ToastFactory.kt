package com.magicalrice.adolph.wallpaper.utils.toast

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat

object ToastFactory {
    fun makeToast(context: Context, text: CharSequence, duration: Int): IToast {
        return if (NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            SystemToast(makeNormalToast(context, text, duration))
        } else ToastWithoutNotification(makeNormalToast(context, text, duration))
    }

    fun newToast(context: Context): IToast {
        return if (NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            SystemToast(Toast(context))
        } else ToastWithoutNotification(Toast(context))
    }

    private fun makeNormalToast(context: Context, text: CharSequence, duration: Int): Toast {
        @SuppressLint("ShowToast")
        val toast = Toast.makeText(context, "", duration)
        toast.setText(text)
        return toast
    }
}