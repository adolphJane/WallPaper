package com.magicalrice.adolph.wallpaper.view.viewer

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.magicalrice.adolph.wallpaper.R

class WallpaperBrowserDialogFragment : DialogFragment(), View.OnClickListener {
    private lateinit var viewModel: WallpaperViewerViewModel
    private lateinit var tvLock: TextView
    private lateinit var tvMain: TextView
    private lateinit var tvCancel: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(WallpaperViewerViewModel::class.java)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(activity!!, R.style.BottomDialog)
        val view = LayoutInflater.from(activity).inflate(R.layout.fragment_dialog_wallpaper_browser,null,false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvLock = view.findViewById(R.id.tv_lock_screen)
            tvLock.setOnClickListener(this)
        }
        tvMain = view.findViewById(R.id.tv_main_screen)
        tvCancel = view.findViewById(R.id.tv_cancel)

        tvMain.setOnClickListener(this)
        tvCancel.setOnClickListener(this)

        dialog.setContentView(view)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        val window = dialog.window
        val params = window?.attributes
        window?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(context!!, android.R.color.transparent)))
        params?.gravity = Gravity.BOTTOM
        params?.width = WindowManager.LayoutParams.MATCH_PARENT
        params?.height = WindowManager.LayoutParams.WRAP_CONTENT
        window?.attributes = params
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.tv_lock_screen -> {
                viewModel.setWallpaper(1,activity as WallpaperViewerActivity?)
            }
            R.id.tv_main_screen -> {
                viewModel.setWallpaper(2,activity as WallpaperViewerActivity?)
            }
            R.id.tv_cancel -> dismissAllowingStateLoss()
        }
    }

    companion object {
        fun start(activity: FragmentActivity?) : WallpaperBrowserDialogFragment{
            var fragment: WallpaperBrowserDialogFragment? = activity?.supportFragmentManager?.findFragmentByTag("browser") as WallpaperBrowserDialogFragment?
            if (fragment != null) {
                fragment.dismissAllowingStateLoss()
                activity?.supportFragmentManager?.beginTransaction()?.remove(fragment)?.commitAllowingStateLoss()
            }

            fragment = WallpaperBrowserDialogFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            if (activity?.isFinishing == false) {
                activity.supportFragmentManager.beginTransaction().add(fragment,"browser").commitAllowingStateLoss()
            }
            return fragment
        }
    }
}