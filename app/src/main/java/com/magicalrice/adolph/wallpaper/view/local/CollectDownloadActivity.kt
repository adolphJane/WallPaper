package com.magicalrice.adolph.wallpaper.view.local

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.jaeger.library.StatusBarUtil
import com.magicalrice.adolph.wallpaper.R
import com.magicalrice.adolph.wallpaper.adapter.ViewPagerAdapter
import com.magicalrice.adolph.wallpaper.databinding.ActivityWallpaperCollectBinding
import com.magicalrice.adolph.wallpaper.view.base.BaseActivity
import com.magicalrice.adolph.wallpaper.view.main.MainFragment

/**
 * @package com.magicalrice.adolph.wallpaper.view.collect
 * @author Adolph
 * @date 2019-05-20 Mon
 * @description TODO
 */

class CollectDownloadActivity(override val layoutId: Int = R.layout.activity_wallpaper_collect) : BaseActivity() {
    private lateinit var pagerAdapter: ViewPagerAdapter
    private lateinit var binding: ActivityWallpaperCollectBinding
    private var type: Int = 1           //1-收藏 2-下载
    override fun onInit(savedInstanceState: Bundle?) {
        type = intent.getIntExtra("type", 1)

        binding = getDataBinding()
        binding.type = type

        StatusBarUtil.setColorNoTranslucent(this,ContextCompat.getColor(this, R.color.gray2))
        initTabPager()
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun initTabPager() {
        val fragmentList = ArrayList<Fragment>()
        val desktopFragment = LocalFragment()
        val desktopBundle = Bundle()
        desktopBundle.putInt("type",type)
        desktopBundle.putInt("wallpaperType",1)
        desktopFragment.arguments = desktopBundle
        fragmentList.add(desktopFragment)

        val phoneFragment = LocalFragment()
        val phoneBundle = Bundle()
        phoneBundle.putInt("type",type)
        phoneBundle.putInt("wallpaperType",2)
        phoneFragment.arguments = phoneBundle
        fragmentList.add(phoneFragment)

        val titleList = arrayListOf<String>()
        titleList.add("桌面壁纸")
        titleList.add("手机壁纸")

        pagerAdapter = ViewPagerAdapter(
            supportFragmentManager,
            titleList,
            fragmentList
        )

        binding.vpCollect.adapter = pagerAdapter
        binding.vpCollect.offscreenPageLimit = 1
        binding.vpCollect.currentItem = 0
        binding.tabCollect.setupWithViewPager(binding.vpCollect)
    }

    companion object {
        fun start(activity: FragmentActivity?, type: Int) {
            val intent = Intent(activity, CollectDownloadActivity::class.java)
            intent.putExtra("type", type)
            activity?.startActivity(intent)
        }
    }
}