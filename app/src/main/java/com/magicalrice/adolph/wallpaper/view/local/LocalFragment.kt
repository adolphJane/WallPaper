package com.magicalrice.adolph.wallpaper.view.local

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.magicalrice.adolph.wallpaper.R
import com.magicalrice.adolph.wallpaper.adapter.WallpaperCollectAdapter
import com.magicalrice.adolph.wallpaper.adapter.WallpaperDownloadAdapter
import com.magicalrice.adolph.wallpaper.bean.GalleryImageBean
import com.magicalrice.adolph.wallpaper.bean.WallpaperCollectBean
import com.magicalrice.adolph.wallpaper.databinding.FragmentLocalBinding
import com.magicalrice.adolph.wallpaper.view.base.BaseFragment
import com.magicalrice.adolph.wallpaper.view.viewer.WallpaperViewerActivity
import com.magicalrice.adolph.wallpaper.widget.EmptyView
import com.magicalrice.adolph.wallpaper.widget.WallpaperHomeView
import java.io.File

/**
 * @package com.magicalrice.adolph.wallpaper.view.local
 * @author Adolph
 * @date 2019-05-20 Mon
 * @description TODO
 */

class LocalFragment : BaseFragment<FragmentLocalBinding>() {
    private lateinit var viewModule: LocalViewModel
    private lateinit var collectAdapter: WallpaperCollectAdapter
    private lateinit var downloadAdapter: WallpaperDownloadAdapter
    private var type: Int = 1
    private var wallpaperType: Int = 1

    override val layoutId: Int
        get() = R.layout.fragment_local

    override fun onInit(savedInstanceState: Bundle?) {
        type = arguments?.getInt("type", 1) ?: 1
        wallpaperType = arguments?.getInt("wallpaperType", 1) ?: 1

        viewModule = ViewModelProviders.of(this).get(LocalViewModel::class.java)

        if (type == 1) {
            if (wallpaperType == 1) {
                collectAdapter = WallpaperCollectAdapter(
                    R.layout.layout_item_home_horizonal_wallpaper,
                    arrayListOf()
                )
                getBinding().ryLocalList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            } else {
                collectAdapter = WallpaperCollectAdapter(
                    R.layout.layout_item_home_vertical_wallpaper,
                    arrayListOf()
                )
                getBinding().ryLocalList.layoutManager = GridLayoutManager(context, 2)
            }
            val empty = EmptyView(activity)
            empty.setTitle("暂无收藏图片，快去收藏吧!")
            empty.setImage(R.drawable.ic_empty)
            collectAdapter.emptyView = empty

            getBinding().ryLocalList.adapter = collectAdapter

            collectAdapter.setOnItemClickListener { adapter, view, position ->
                if (position < adapter.data.size) {
                    val data = adapter.data[position] as WallpaperCollectBean
                    val gallery = GalleryImageBean(data.imgPath,0,0)
                    val image = (view.findViewById(R.id.homeView) as WallpaperHomeView).getImage()
                    WallpaperViewerActivity.start(activity,gallery,image,wallpaperType,false)
                }
            }
        } else {
            if (wallpaperType == 1) {
                downloadAdapter = WallpaperDownloadAdapter(
                    R.layout.layout_item_home_horizonal_wallpaper,
                    arrayListOf()
                )
                getBinding().ryLocalList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            } else {
                downloadAdapter = WallpaperDownloadAdapter(
                    R.layout.layout_item_home_vertical_wallpaper,
                    arrayListOf()
                )
                getBinding().ryLocalList.layoutManager = GridLayoutManager(context, 2)
            }

            val empty = EmptyView(activity)
            empty.setTitle("暂无下载图片，快去下载吧!")
            empty.setImage(R.drawable.ic_empty)
            downloadAdapter.emptyView = empty

            getBinding().ryLocalList.adapter = downloadAdapter

            downloadAdapter.setOnItemClickListener { adapter, view, position ->
                if (position < adapter.data.size) {
                    val data = adapter.data[position] as File
                    val gallery = GalleryImageBean(data.absolutePath,0,0)
                    val image = (view.findViewById(R.id.homeView) as WallpaperHomeView).getImage()
                    WallpaperViewerActivity.start(activity,gallery,image,wallpaperType,true)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (type == 1) {
            viewModule.loadCollect(wallpaperType).observe(this, Observer {
                collectAdapter.setNewData(it)
            })
        } else {
            viewModule.loadDownload(wallpaperType,activity)?.let {
                downloadAdapter.setNewData(it)
            }
        }
    }

    override fun lazyInitData() {
    }
}