package com.magicalrice.adolph.wallpaper.view.viewer

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
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
    private var wallpaperType: Int = 1
    private var isDownload: Boolean = false

    override fun onInit(savedInstanceState: Bundle?) {
        imgBean = intent.getParcelableExtra("img")
        wallpaperType = intent.getIntExtra("wallpaperType",1)
        isDownload = intent.getBooleanExtra("isDownload",false)
        viewModel = ViewModelProvider(this).get(WallpaperViewerViewModel::class.java)
        binding = getDataBinding()

        StatusBarUtil.setTranslucentForImageView(this,0,null)

        binding.listener = this
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.isDownload = isDownload

        viewModel.loadCurrentWallpaper(imgBean?.imgSrc ?: "",this)
        viewModel.initAnimator(binding.llDownload,binding.rlBottombar,isDownload)

        GlideApp.with(this)
            .asBitmap()
            .load(imgBean?.imgSrc)
            .into(binding.imgView)
    }

    override fun onBrowser() {
        viewModel.openBrowser(this)
    }

    override fun onDownload() {
        viewModel.downloadWallpaper(this, wallpaperType)
    }

    override fun onCollect() {
        viewModel.collectWallpaper(this, wallpaperType)
    }

    override fun onDelete() {
        viewModel.deleteWallpaper(this, imgBean?.imgSrc ?: "")
    }

    override fun showWallpaper() {
        viewModel.showWallpaper()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.dismiss()
    }

    fun dismissBrowser() {
        viewModel.dismissDialog()
    }

    companion object {
        fun start(activity: FragmentActivity?, imgBean: GalleryImageBean, shareView: ImageView, wallpaperType: Int, isDownload: Boolean) {
            val intent = Intent(activity, WallpaperViewerActivity::class.java)
            intent.putExtra("img", imgBean)
            intent.putExtra("wallpaperType", wallpaperType)
            intent.putExtra("isDownload", isDownload)
            activity?.let {
                val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,shareView,activity.getString(R.string.transition_image_name)).toBundle()
                activity.startActivity(intent,bundle)
            }
        }
    }
}