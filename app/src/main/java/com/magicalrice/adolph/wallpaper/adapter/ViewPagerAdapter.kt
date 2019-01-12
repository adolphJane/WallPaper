package com.magicalrice.adolph.wallpaper.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class ViewPagerAdapter : FragmentPagerAdapter {
    private var data: ArrayList<String>
    private var fragmentList: ArrayList<Fragment>

    constructor(fm: FragmentManager,data: ArrayList<String>, fragmentList: ArrayList<Fragment>) : super(fm) {
        this.data = data
        this.fragmentList = fragmentList
    }

    override fun getItem(p0: Int): Fragment {
        return fragmentList[p0]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return data[position]
    }
}