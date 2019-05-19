package com.magicalrice.adolph.wallpaper.view.viewer

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.jaeger.library.StatusBarUtil
import com.magicalrice.adolph.wallpaper.R
import com.magicalrice.adolph.wallpaper.bean.GalleryImageBean
import com.magicalrice.adolph.wallpaper.databinding.ActivityWallpaperViewerBinding
import com.magicalrice.adolph.wallpaper.view.base.BaseActivity
import com.magicalrice.adolph.wallpaper.widget.GlideApp

class WallpaperViewerActivity(override val layoutId: Int = R.layout.activity_wallpaper_viewer) : BaseActivity(),WallpaperViewerListener {
    private lateinit var binding: ActivityWallpaperViewerBinding
    private lateinit var viewModel: WallpaperViewerViewModel
    private var imgBean: GalleryImageBean? = null
    override fun onInit(savedInstanceState: Bundle?) {
        imgBean = intent.getParcelableExtra("img")
        viewModel = ViewModelProviders.of(this).get(WallpaperViewerViewModel::class.java)
        binding = getDataBinding()

        StatusBarUtil.setTranslucentForImageView(this,0,null)

        binding.listener = this
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.loadCurrentWallpaper(imgBean?.imgSrc ?: "",this)
        viewModel.initAnimator(binding.rlTopbar,binding.rlBottombar)

        GlideApp.with(this)
            .asBitmap()
            .load(imgBean?.imgSrc)
            .centerInside()
            .into(binding.imgView)
    }

    override fun onBrowser() {
        viewModel.openBrowser(this)
    }

    override fun onDownload() {
        viewModel.downloadWallpaper(this)
    }

    override fun onCollect() {
        viewModel.collectWallpaper(this)
    }

    override fun showWallpaper() {
        viewModel.showWallpaper()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.dismiss()
    }

    companion object {
        fun start(activity: FragmentActivity, imgBean: GalleryImageBean, shareView: ImageView) {
            val intent = Intent(activity, WallpaperViewerActivity::class.java)
            intent.putExtra("img", imgBean)
            val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,shareView,activity.getString(R.string.transition_image_name)).toBundle()
            activity.startActivity(intent,bundle)
        }
    }
}