package com.magicalrice.adolph.wallpaper.view.viewer

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.FragmentActivity
import android.view.View
import android.widget.ImageView
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
        viewModel.downloadWallpaper()
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