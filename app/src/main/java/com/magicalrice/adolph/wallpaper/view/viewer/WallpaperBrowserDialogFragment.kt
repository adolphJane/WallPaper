package com.magicalrice.adolph.wallpaper.view.viewer

import android.app.Dialog
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentActivity
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView
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
        tvLock = view.findViewById(R.id.tv_lock_screen)
        tvMain = view.findViewById(R.id.tv_main_screen)
        tvCancel = view.findViewById(R.id.tv_cancel)

        tvLock.setOnClickListener(this)
        tvMain.setOnClickListener(this)
        tvCancel.setOnClickListener(this)

        dialog.setContentView(view)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        val window = dialog.window
        val params = window?.attributes
        params?.gravity = Gravity.BOTTOM
        params?.width = WindowManager.LayoutParams.MATCH_PARENT
        params?.height = WindowManager.LayoutParams.WRAP_CONTENT
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.tv_lock_screen -> {

            }
            R.id.tv_main_screen -> {

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