package com.magicalrice.adolph.wallpaper.view.collection

import android.animation.ArgbEvaluator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.chad.library.adapter.base.BaseViewHolder
import com.jaeger.library.StatusBarUtil
import com.magicalrice.adolph.wallpaper.R
import com.magicalrice.adolph.wallpaper.adapter.GalleryCollectionAdapter
import com.magicalrice.adolph.wallpaper.bean.GalleryImageBean
import com.magicalrice.adolph.wallpaper.databinding.ActivityWallpaperCollectionBinding
import com.magicalrice.adolph.wallpaper.databinding.ActivityWallpaperViewerBinding
import com.magicalrice.adolph.wallpaper.view.base.BaseActivity
import com.magicalrice.adolph.wallpaper.view.viewer.WallpaperViewerActivity
import com.yarolegovich.discretescrollview.DSVOrientation
import com.yarolegovich.discretescrollview.DiscreteScrollView

class WallpaperCollectionActivity(override val layoutId: Int = R.layout.activity_wallpaper_collection) :
    BaseActivity(),
    DiscreteScrollView.ScrollListener<BaseViewHolder>,
    DiscreteScrollView.OnItemChangedListener<BaseViewHolder> {

    private lateinit var viewModel: WallpaperCollectionViewModel
    private lateinit var adapter: GalleryCollectionAdapter
    private lateinit var binding: ActivityWallpaperCollectionBinding
    private lateinit var evaluator: ArgbEvaluator
    private var data: ArrayList<GalleryImageBean> = arrayListOf()
    private var currentOverlayColor: Int = 0
    private var overlayColor: Int = 0
    private var href = ""
    private var title: String = ""
    private var wallpaperType = 1

    override fun onInit(savedInstanceState: Bundle?) {
        wallpaperType = intent.getIntExtra("wallpaperType", 1)
        href = intent.getStringExtra("href")
        title = intent.getStringExtra("title")
        viewModel = ViewModelProviders.of(this).get(WallpaperCollectionViewModel::class.java)
        binding = getDataBinding()
        viewModel.getWallpaperCollectionNum(href, this)

        StatusBarUtil.setTranslucentForImageView(this,0,binding.btnBack)
        binding.btnBack.text = title
        binding.btnBack.setOnClickListener {
            finish()
        }

        initGallery()
        initData()
    }

    private fun initGallery() {
        evaluator = ArgbEvaluator()
        currentOverlayColor = ContextCompat.getColor(this, R.color.galleryCurrentItemOverlay)
        overlayColor = ContextCompat.getColor(this, R.color.galleryItemOverlay)

        binding.rvList.addScrollListener(this)
        binding.rvList.addOnItemChangedListener(this)
        binding.rvList.setOrientation(if (wallpaperType == 1) DSVOrientation.VERTICAL else DSVOrientation.HORIZONTAL)
        binding.rvList.scrollToPosition(1)
    }

    private fun initData() {
        viewModel.wallpaperList.observe(this, Observer {
            it?.let {
                if (binding.rvList.alpha == 0f) {
                    binding.rvList.animate().alpha(0f).alpha(1f).setDuration(1000).start()
                }
                data.clear()
                data.addAll(it)
                binding.tvNumMark.visibility = View.VISIBLE
                setNumMark(1)
                initAdapter()
            }
        })
    }

    private fun initAdapter() {
        adapter = GalleryCollectionAdapter(R.layout.item_gallery,data,wallpaperType)
        adapter.openLoadAnimation()
        adapter.setOnItemChildClickListener { adapter, view, position ->
            if (view.id == R.id.img_root) {
                val bean = adapter.data[position] as GalleryImageBean
                if (binding.rvList.currentItem == position) {
                    WallpaperViewerActivity.start(this,bean,view.findViewById(R.id.image), wallpaperType, false)
                } else {
                    binding.rvList.smoothScrollToPosition(position)
                }
            }
        }
        binding.rvList.adapter = adapter
    }

    override fun onScroll(
        scrollPosition: Float,
        currentPosition: Int,
        newPosition: Int,
        currentHolder: BaseViewHolder?,
        newCurrent: BaseViewHolder?
    ) {
        if (currentHolder != null && newCurrent != null) {
            val position = Math.abs(scrollPosition)
            currentHolder.getView<View>(R.id.overlay).setBackgroundColor(interpolate(position, currentOverlayColor, overlayColor))
            newCurrent.getView<View>(R.id.overlay).setBackgroundColor(interpolate(position, overlayColor, currentOverlayColor))
        }
    }

    override fun onCurrentItemChanged(
        viewHolder: BaseViewHolder?,
        adapterPosition: Int
    ) {
        viewHolder?.let {
            it.getView<View>(R.id.overlay).setBackgroundColor(currentOverlayColor)
            setNumMark(adapterPosition + 1)
        }
    }

    private fun interpolate(fraction: Float, c1: Int, c2: Int): Int {
        return evaluator.evaluate(fraction, c1, c2) as Int
    }

    private fun setNumMark(curPos: Int) {
        val mark = "$curPos / ${data.size}"
        binding.tvNumMark.text = mark
    }

    companion object {
        fun start(activity: FragmentActivity?, href: String, wallpaperType: Int, title: String) {
            val intent = Intent(activity, WallpaperCollectionActivity::class.java)
            intent.putExtra("href", href)
            intent.putExtra("wallpaperType", wallpaperType)
            intent.putExtra("title", title)
            activity?.startActivity(intent)
        }
    }
}