package com.magicalrice.adolph.wallpaper.view.main

import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
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
import com.magicalrice.adolph.wallpaper.view.local.CollectDownloadActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity(override val layoutId: Int = R.layout.activity_main) : BaseActivity(),
    MainListener {
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
    private var dp10 = 0
    private var dp20 = 0

    override fun onInit(savedInstanceState: Bundle?) {
        binding = getDataBinding()
        viewModule = ViewModelProviders.of(this).get(MainViewModule::class.java)
        initToolbar()
        initView()
        initTypeView()
        initFab()
    }

    private fun initView() {
        dp10 = ScreenUtils.dp2px(this, 10f)
        dp20 = ScreenUtils.dp2px(this, 20f)
        initFilterView()
        initTabPager()
        binding.fabColor.setImageResource(R.drawable.ic_home_color)
        binding.fabSize.setImageResource(R.drawable.ic_home_size)
        binding.fabStyle.setImageResource(R.drawable.ic_home_style)
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

        StatusBarUtil.setTranslucentForCoordinatorLayout(this, 0)
    }

    private fun initFilterView() {
        filterList = RecyclerView(this)
        filterList.setPadding(dp10, dp20, dp10, dp10)

        styleAdapter = WallpaperFilterAdapter(
            R.layout.layout_item_filter,
            Utils.getWallpaperStyle(wallpaperType)
        )
        styleAdapter.setOnItemChildClickListener { adapter, _, position ->
            styleAdapter.clearData(position)
            viewModule.setData((adapter.data[position] as WallpaperFilterBean).id, 3, wallpaperType)
        }
        val styleText = TextView(this)
        styleText.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        styleText.text = "壁纸分类"
        styleText.setPadding(dp10, 0, 0, dp20)
        styleText.textSize = 16f
        styleText.setTextColor(ContextCompat.getColor(this, R.color.white1))
        styleAdapter.addHeaderView(styleText)

        sizeAdapter = WallpaperFilterAdapter(
            R.layout.layout_item_filter,
            Utils.getWallpaperSize(wallpaperType)
        )
        sizeAdapter.setOnItemChildClickListener { adapter, _, position ->
            sizeAdapter.clearData(position)
            viewModule.setData((adapter.data[position] as WallpaperFilterBean).id, 2, wallpaperType)
        }
        val sizeText = TextView(this)
        sizeText.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        sizeText.text = "壁纸尺寸"
        sizeText.textSize = 16f
        sizeText.setTextColor(ContextCompat.getColor(this, R.color.white1))
        sizeText.setPadding(dp10, 0, 0, dp20)
        sizeAdapter.addHeaderView(sizeText)

        colorAdapter = WallpaperFilterAdapter(
            R.layout.layout_item_filter,
            Utils.getWallpaperColor(wallpaperType)
        )
        colorAdapter.setOnItemChildClickListener { adapter, _, position ->
            colorAdapter.clearData(position)
            viewModule.setData((adapter.data[position] as WallpaperFilterBean).id, 1, wallpaperType)
        }
        val colorText = TextView(this)
        colorText.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        colorText.text = "壁纸颜色"
        colorText.textSize = 16f
        colorText.setTextColor(ContextCompat.getColor(this, R.color.white1))
        colorText.setPadding(dp10, 0, 0, dp20)
        colorAdapter.addHeaderView(colorText)
    }

    private fun initTabPager() {
        val fragmentList = ArrayList<Fragment>()
        val phoneFragment = MainFragment()
        val phoneBundle = Bundle()
        phoneBundle.putInt("wallpaperType", 2)
        phoneFragment.arguments = phoneBundle
        fragmentList.add(phoneFragment)

        val desktopFragment = MainFragment()
        val desktopBundle = Bundle()
        desktopBundle.putInt("wallpaperType", 1)
        desktopFragment.arguments = desktopBundle
        fragmentList.add(desktopFragment)

        val titleList = arrayListOf<String>()
        titleList.add("手机壁纸")
        titleList.add("桌面壁纸")

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
        if (binding.drawLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawLayout.closeDrawer(GravityCompat.START)
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
        filterList.layoutManager = GridLayoutManager(this, 3)
        filterList.adapter = colorAdapter
        val params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        SnackbarUtils.with(binding.menuFilter).setDuration(SnackbarUtils.LENGTH_INDEFINITE).show()
        SnackbarUtils.removeView(filterList)
        SnackbarUtils.addView(filterList, params)
    }

    override fun choiceStyle() {
        filterList.layoutManager = GridLayoutManager(this, 4)
        filterList.adapter = styleAdapter
        val params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        SnackbarUtils.with(binding.menuFilter).setDuration(SnackbarUtils.LENGTH_INDEFINITE).show()
        SnackbarUtils.removeView(filterList)
        SnackbarUtils.addView(filterList, params)
    }

    override fun choiceSize() {
        filterList.layoutManager = GridLayoutManager(this, 3)
        filterList.adapter = sizeAdapter
        val params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        SnackbarUtils.with(binding.menuFilter).setDuration(SnackbarUtils.LENGTH_INDEFINITE).show()
        SnackbarUtils.removeView(filterList)
        SnackbarUtils.addView(filterList, params)
    }

    override fun skipToCollection() {
        CollectDownloadActivity.start(this, 1)
    }

    override fun skipToDownload() {
        CollectDownloadActivity.start(this, 2)
    }
}
