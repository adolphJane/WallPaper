package com.magicalrice.adolph.wallpaper.view.main

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.jaeger.library.StatusBarUtil
import com.magicalrice.adolph.wallpaper.R
import com.magicalrice.adolph.wallpaper.adapter.ViewPagerAdapter
import com.magicalrice.adolph.wallpaper.adapter.WallpaperFilterAdapter
import com.magicalrice.adolph.wallpaper.bean.WallpaperFilterBean
import com.magicalrice.adolph.wallpaper.databinding.ActivityMainBinding
import com.magicalrice.adolph.wallpaper.utils.ScreenUtils
import com.magicalrice.adolph.wallpaper.utils.SnackbarUtils
import com.magicalrice.adolph.wallpaper.utils.Utils
import com.magicalrice.adolph.wallpaper.view.base.BaseActivity
import com.magicalrice.adolph.wallpaper.widget.FlexibleFlexboxLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity(override val layoutId: Int = R.layout.activity_main) : BaseActivity(),MainListener {

    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var viewModule: MainViewModule
    private lateinit var styleAdapter: WallpaperFilterAdapter
    private lateinit var sizeAdapter: WallpaperFilterAdapter
    private lateinit var colorAdapter: WallpaperFilterAdapter
    private lateinit var filterList: RecyclerView
    private lateinit var pagerAdapter: ViewPagerAdapter
    private var wallpaperType = 1

    override fun onInit(savedInstanceState: Bundle?) {
        binding = getDataBinding()
        viewModule = ViewModelProviders.of(this).get(MainViewModule::class.java)
        initToolbar()
        initView()
        initTypeView()
        initFab()
    }

    private fun initView() {
        initFilterView()
        initTabPager()
    }

    private fun initFab() {
        binding.menuFilter.setOnMenuButtonClickListener {
            if (binding.menuFilter.isOpened) {
                binding.menuFilter.toggle(true)
                SnackbarUtils.dismiss()
            } else {
                binding.menuFilter.toggle(true)
            }
        }
    }

    private fun initToolbar() {
        tool_bar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorAccent))
        setSupportActionBar(tool_bar)

        supportActionBar?.setHomeButtonEnabled(true)                               //设置返回键可用
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle = ActionBarDrawerToggle(this, draw_layout, tool_bar, 0, 0)
        toggle.syncState()
        draw_layout.addDrawerListener(toggle)

        StatusBarUtil.setTranslucentForCoordinatorLayout(this,0)
    }

    private fun initFilterView() {
        filterList = RecyclerView(this)
        filterList.setPadding(ScreenUtils.dp2px(this,10f),ScreenUtils.dp2px(this,20f),ScreenUtils.dp2px(this,10f),ScreenUtils.dp2px(this,10f))
        val layoutManager = FlexibleFlexboxLayoutManager(this)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.flexWrap = FlexWrap.WRAP
        filterList.layoutManager = layoutManager

        styleAdapter = WallpaperFilterAdapter(R.layout.layout_item_filter, Utils.getWallpaperStyle(wallpaperType))
        styleAdapter.setOnItemChildClickListener { adapter, _, position ->
            styleAdapter.clearData(position)
            viewModule.setData((adapter.data[position] as WallpaperFilterBean).id,3,wallpaperType)
        }

        sizeAdapter = WallpaperFilterAdapter(R.layout.layout_item_filter, Utils.getWallpaperSize(wallpaperType))
        sizeAdapter.setOnItemChildClickListener { adapter, _, position ->
            sizeAdapter.clearData(position)
            viewModule.setData((adapter.data[position] as WallpaperFilterBean).id,2,wallpaperType)
        }

        colorAdapter = WallpaperFilterAdapter(R.layout.layout_item_filter, Utils.getWallpaperColor(wallpaperType))
        colorAdapter.setOnItemChildClickListener { adapter, _, position ->
            colorAdapter.clearData(position)
            viewModule.setData((adapter.data[position] as WallpaperFilterBean).id,1,wallpaperType)
        }
    }

    private fun initTabPager() {
        val fragmentList = ArrayList<Fragment>()
        val desktopFragment = MainFragment()
        val desktopBundle = Bundle()
        desktopBundle.putInt("wallpaperType",1)
        desktopFragment.arguments = desktopBundle
        fragmentList.add(desktopFragment)

        val phoneFragment = MainFragment()
        val phoneBundle = Bundle()
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

        binding.vpMain.adapter = pagerAdapter
        binding.vpMain.offscreenPageLimit = 1
        binding.vpMain.currentItem = 0
        binding.tabMain.setupWithViewPager(binding.vpMain)
        binding.tabMain.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                p0?.run {
                    if (text?.equals("桌面壁纸") == true) {
                        selectWallpaperType(1)
                    } else if (text?.equals("手机壁纸") == true) {
                        selectWallpaperType(2)
                    }
                }
            }

        })
    }

    override fun onPause() {
        super.onPause()
        if (binding.menuFilter.isOpened) {
            binding.menuFilter.toggle(true)
            SnackbarUtils.dismiss()
        }
    }

    private fun initTypeView() {
        binding.listener = this
    }

    fun selectWallpaperType(type: Int) {
        wallpaperType = type
        styleAdapter.setNewData(Utils.getWallpaperStyle(wallpaperType))
        colorAdapter.setNewData(Utils.getWallpaperColor(wallpaperType))
        sizeAdapter.setNewData(Utils.getWallpaperSize(wallpaperType))
    }

    override fun choiceColor() {
        filterList.adapter = colorAdapter
        val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        SnackbarUtils.with(binding.menuFilter).setDuration(SnackbarUtils.LENGTH_INDEFINITE).show()
        SnackbarUtils.removeView(filterList)
        SnackbarUtils.addView(filterList,params)
    }

    override fun choiceStyle() {
        filterList.adapter = styleAdapter
        val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        SnackbarUtils.with(binding.menuFilter).setDuration(SnackbarUtils.LENGTH_INDEFINITE).show()
        SnackbarUtils.removeView(filterList)
        SnackbarUtils.addView(filterList,params)
    }

    override fun choiceSize() {
        filterList.adapter = sizeAdapter
        val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        SnackbarUtils.with(binding.menuFilter).setDuration(SnackbarUtils.LENGTH_INDEFINITE).show()
        SnackbarUtils.removeView(filterList)
        SnackbarUtils.addView(filterList,params)
    }
}