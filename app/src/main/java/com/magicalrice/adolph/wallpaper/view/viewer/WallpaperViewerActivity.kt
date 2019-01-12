package com.magicalrice.adolph.wallpaper.view.viewer

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.FragmentActivity
import android.widget.ImageView
import com.jaeger.library.StatusBarUtil
import com.magicalrice.adolph.wallpaper.R
import com.magicalrice.adolph.wallpaper.bean.GalleryImageBean
import com.magicalrice.adolph.wallpaper.databinding.ActivityWallpaperViewerBinding
import com.magicalrice.adolph.wallpaper.view.base.BaseActivity
import com.magicalrice.adolph.wallpaper.widget.GlideApp

class WallpaperViewerActivity(override val layoutId: Int = R.layout.activity_wallpaper_viewer) : BaseActivity() {
    private lateinit var binding: ActivityWallpaperViewerBinding
    private var imgBean: GalleryImageBean? = null
    override fun onInit(savedInstanceState: Bundle?) {
        imgBean = intent.getParcelableExtra("img")
        binding = getDataBinding()

        StatusBarUtil.setTranslucentForImageView(this,0,null)

        GlideApp.with(this)
            .asBitmap()
            .load(imgBean?.imgSrc)
            .centerInside()
            .into(binding.imgView)

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