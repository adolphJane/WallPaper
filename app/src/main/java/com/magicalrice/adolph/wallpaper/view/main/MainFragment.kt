package com.magicalrice.adolph.wallpaper.view.main

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.magicalrice.adolph.wallpaper.R
import com.magicalrice.adolph.wallpaper.adapter.WallpaperHomeAdapter
import com.magicalrice.adolph.wallpaper.bean.WallpaperBean
import com.magicalrice.adolph.wallpaper.databinding.FragmentMainBinding
import com.magicalrice.adolph.wallpaper.view.base.BaseFragment
import com.magicalrice.adolph.wallpaper.view.collection.WallpaperCollectionActivity
import com.magicalrice.adolph.wallpaper.widget.RefreshLottieHeader

class MainFragment : BaseFragment<FragmentMainBinding>() {
    private lateinit var viewModule: MainViewModule
    private lateinit var homeAdapter: WallpaperHomeAdapter
    private var wallpaperType: Int = 1
    override val layoutId: Int
        get() = R.layout.fragment_main

    override fun onInit(savedInstanceState: Bundle?) {
        viewModule = ViewModelProvider(activity!!).get(MainViewModule::class.java)
        wallpaperType = arguments?.getInt("wallpaperType") ?: 1
        initHeader()
        showWallPaper()
        initData()
    }

    private fun initData() {
        viewModule.desktopSelect.observe(this, Observer {
            if (wallpaperType == 1) {
                getBinding().smartRefresh.autoRefresh()
            }
        })

        viewModule.phoneSelect.observe(this, Observer {
            if (wallpaperType == 2) {
                getBinding().smartRefresh.autoRefresh()
            }
        })
    }

    private fun initHeader() {
        context?.let {
            val view = RefreshLottieHeader(it)
            getBinding().smartRefresh.setRefreshHeader(view)
            getBinding().smartRefresh.setOnRefreshListener {
                loadNewData()
            }
        }
    }

    override fun lazyInitData() {

    }

    private fun showWallPaper() {
        if (wallpaperType == 1) {
            homeAdapter =
                    WallpaperHomeAdapter(R.layout.layout_item_home_horizonal_wallpaper, ArrayList())
        } else if (wallpaperType == 2) {
            homeAdapter =
                    WallpaperHomeAdapter(R.layout.layout_item_home_vertical_wallpaper, ArrayList())
        }
        homeAdapter.setEnableLoadMore(true)
        homeAdapter.setOnLoadMoreListener({
            loadData(false)
        }, getBinding().ryList)
        getBinding().ryList.adapter = homeAdapter
        if (wallpaperType == 1) {
            getBinding().ryList.layoutManager =
                    LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        } else {
            getBinding().ryList.layoutManager = GridLayoutManager(context, 2)
        }
        homeAdapter.openLoadAnimation()
        homeAdapter.setOnItemClickListener { adapter, _, position ->
            if (position < adapter.data.size) {
                val data = adapter.data[position] as WallpaperBean
                WallpaperCollectionActivity.start(activity,data.href,wallpaperType,data.title)
            }
        }
        loadNewData()
    }

    private fun loadNewData() {
        loadData(true)
    }

    private fun loadData(isRefresh: Boolean) {
        activity?.let {
            viewModule.refreshData(
                viewModule.getWallpaper(it,wallpaperType, isRefresh),
                homeAdapter,
                getBinding().smartRefresh,
                isRefresh
            )
        }
    }
}