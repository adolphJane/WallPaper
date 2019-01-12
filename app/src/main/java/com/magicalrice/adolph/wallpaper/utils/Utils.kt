package com.magicalrice.adolph.wallpaper.utils

import com.magicalrice.adolph.wallpaper.bean.WallpaperFilterBean

object Utils {
    private val desktopTypeList = ArrayList<WallpaperFilterBean>()
    private val phoneTypeList = ArrayList<WallpaperFilterBean>()
    private val desktopSizeList = ArrayList<WallpaperFilterBean>()
    private val phoneSizeList = ArrayList<WallpaperFilterBean>()
    private val desktopColorList = ArrayList<WallpaperFilterBean>()
    private val phoneColorList = ArrayList<WallpaperFilterBean>()

    fun getWallpaperStyle(wallPaper: Int) : ArrayList<WallpaperFilterBean> {
        when(wallPaper) {
            1 -> return getDeskType()
            2 -> return getPhoneType()
            1 -> return getDeskType()
            1 -> return getDeskType()
            1 -> return getDeskType()
            1 -> return getDeskType()
            else -> return getDeskType()
        }
    }

    fun getDeskType() : ArrayList<WallpaperFilterBean>{
        if (desktopTypeList.size == 0) {
            desktopTypeList.add(WallpaperFilterBean(0,"全部壁纸",1,isSelect = true))
            desktopTypeList.add(WallpaperFilterBean(191,"游戏壁纸",1))
            desktopTypeList.add(WallpaperFilterBean(192,"卡通动漫",1))
            desktopTypeList.add(WallpaperFilterBean(193,"家居壁纸",1))
            desktopTypeList.add(WallpaperFilterBean(194,"军事壁纸",1))
            desktopTypeList.add(WallpaperFilterBean(195,"汽车壁纸",1))
            desktopTypeList.add(WallpaperFilterBean(196,"广告壁纸",1))
            desktopTypeList.add(WallpaperFilterBean(197,"设计创意",1))
            desktopTypeList.add(WallpaperFilterBean(198,"港台影视",1))
            desktopTypeList.add(WallpaperFilterBean(199,"欧美影视",1))
            desktopTypeList.add(WallpaperFilterBean(200,"日韩影视",1))
            desktopTypeList.add(WallpaperFilterBean(201,"大陆影视",1))
            desktopTypeList.add(WallpaperFilterBean(202,"港台明星",1))
            desktopTypeList.add(WallpaperFilterBean(203,"欧美明星",1))
            desktopTypeList.add(WallpaperFilterBean(204,"日韩明星",1))
            desktopTypeList.add(WallpaperFilterBean(205,"大陆明星",1))
            desktopTypeList.add(WallpaperFilterBean(206,"动物壁纸",1))
            desktopTypeList.add(WallpaperFilterBean(207,"高清壁纸",1))
            desktopTypeList.add(WallpaperFilterBean(208,"风景壁纸",1))
            desktopTypeList.add(WallpaperFilterBean(209,"植物壁纸",1))
            desktopTypeList.add(WallpaperFilterBean(2285,"美女壁纸",1))
            desktopTypeList.add(WallpaperFilterBean(2286,"日历壁纸",1))
            desktopTypeList.add(WallpaperFilterBean(2287,"节日壁纸",1))
            desktopTypeList.add(WallpaperFilterBean(2357,"体育壁纸",1))
            desktopTypeList.add(WallpaperFilterBean(2358,"风格壁纸",1))
            desktopTypeList.add(WallpaperFilterBean(2361,"美食壁纸",1))
        }

        return desktopTypeList
    }

    fun getPhoneType() : ArrayList<WallpaperFilterBean> {
        if (phoneTypeList.size == 0) {
            phoneTypeList.add(WallpaperFilterBean(0,"全部",1,isSelect = true))
            phoneTypeList.add(WallpaperFilterBean(2338,"明星",1))
            phoneTypeList.add(WallpaperFilterBean(2339,"节日",1))
            phoneTypeList.add(WallpaperFilterBean(2340,"美女",1))
            phoneTypeList.add(WallpaperFilterBean(2341,"风景",1))
            phoneTypeList.add(WallpaperFilterBean(2342,"汽车",1))
            phoneTypeList.add(WallpaperFilterBean(2343,"可爱",1))
            phoneTypeList.add(WallpaperFilterBean(2344,"唯美",1))
            phoneTypeList.add(WallpaperFilterBean(2345,"苹果",1))
            phoneTypeList.add(WallpaperFilterBean(2346,"动漫",1))
            phoneTypeList.add(WallpaperFilterBean(2347,"爱情",1))
            phoneTypeList.add(WallpaperFilterBean(2348,"动态",1))
            phoneTypeList.add(WallpaperFilterBean(2349,"卡通",1))
            phoneTypeList.add(WallpaperFilterBean(2350,"搞笑",1))
            phoneTypeList.add(WallpaperFilterBean(2351,"非主流",1))
            phoneTypeList.add(WallpaperFilterBean(2352,"创意",1))
            phoneTypeList.add(WallpaperFilterBean(2353,"游戏",1))
            phoneTypeList.add(WallpaperFilterBean(2354,"影视",1))
            phoneTypeList.add(WallpaperFilterBean(2355,"动物",1))
            phoneTypeList.add(WallpaperFilterBean(2356,"植物",1))
            phoneTypeList.add(WallpaperFilterBean(2359,"风格",1))
            phoneTypeList.add(WallpaperFilterBean(2360,"体育",1))
            phoneTypeList.add(WallpaperFilterBean(2362,"美食",1))
        }
        return phoneTypeList
    }

    fun getWallpaperSize(wallPaper: Int) : ArrayList<WallpaperFilterBean>{
        when(wallPaper) {
            1 -> return getDeskSize()
            2 -> return getPhoneSize()
            1 -> return getDeskType()
            1 -> return getDeskType()
            1 -> return getDeskType()
            1 -> return getDeskType()
            else -> return getDeskType()
        }
    }

    fun getDeskSize() : ArrayList<WallpaperFilterBean>{
        if (desktopSizeList.size == 0) {
            desktopSizeList.add(WallpaperFilterBean(0,"全部尺寸",2,isSelect = true))
            desktopSizeList.add(WallpaperFilterBean(1,"1024*768",2))
            desktopSizeList.add(WallpaperFilterBean(2,"1280*800",2))
            desktopSizeList.add(WallpaperFilterBean(3,"1280*1024",2))
            desktopSizeList.add(WallpaperFilterBean(13,"1366*768",2))
            desktopSizeList.add(WallpaperFilterBean(4,"1440*900",2))
            desktopSizeList.add(WallpaperFilterBean(12,"1600*900",2))
            desktopSizeList.add(WallpaperFilterBean(5,"1600*1200",2))
            desktopSizeList.add(WallpaperFilterBean(6,"1680*1050",2))
            desktopSizeList.add(WallpaperFilterBean(10,"1920*1080",2))
            desktopSizeList.add(WallpaperFilterBean(7,"1920*1200",2))
            desktopSizeList.add(WallpaperFilterBean(8,"1920*1440",2))
            desktopSizeList.add(WallpaperFilterBean(14,"2560*1440",2))
            desktopSizeList.add(WallpaperFilterBean(11,"2560*1600",2))
            desktopSizeList.add(WallpaperFilterBean(9,"其他尺寸",2))
        }

        return desktopSizeList
    }

    fun getPhoneSize() : ArrayList<WallpaperFilterBean>{
        if (phoneSizeList.size == 0) {
            phoneSizeList.add(WallpaperFilterBean(0,"全部尺寸",2,isSelect = true))
            phoneSizeList.add(WallpaperFilterBean(106,"480*720",2))
            phoneSizeList.add(WallpaperFilterBean(107,"480*640",2))
            phoneSizeList.add(WallpaperFilterBean(108,"480*800",2))
            phoneSizeList.add(WallpaperFilterBean(109,"480*854",2))
            phoneSizeList.add(WallpaperFilterBean(110,"540*960",2))
            phoneSizeList.add(WallpaperFilterBean(111,"640*480",2))
            phoneSizeList.add(WallpaperFilterBean(112,"640*960",2))
            phoneSizeList.add(WallpaperFilterBean(113,"640*1136",2))
            phoneSizeList.add(WallpaperFilterBean(114,"720*1080",2))
            phoneSizeList.add(WallpaperFilterBean(115,"720*1280",2))
            phoneSizeList.add(WallpaperFilterBean(116,"768*1280",2))
            phoneSizeList.add(WallpaperFilterBean(117,"800*1280",2))
            phoneSizeList.add(WallpaperFilterBean(118,"750*1334",2))
            phoneSizeList.add(WallpaperFilterBean(119,"1080*1920",2))
            phoneSizeList.add(WallpaperFilterBean(120,"1440*2560",2))
        }

        return desktopSizeList
    }

    fun getWallpaperColor(wallPaper: Int) : ArrayList<WallpaperFilterBean>{
        when(wallPaper) {
            1 -> return getDeskColor()
            2 -> return getPhoneColor()
            1 -> return getDeskType()
            1 -> return getDeskType()
            1 -> return getDeskType()
            1 -> return getDeskType()
            else -> return getDeskType()
        }
    }

    fun getDeskColor() : ArrayList<WallpaperFilterBean>{
        if (desktopColorList.size == 0) {
            desktopColorList.add(WallpaperFilterBean(0,"全部颜色",3,isSelect = true))
            desktopColorList.add(WallpaperFilterBean(1,"黄色",3,"yellow",1))
            desktopColorList.add(WallpaperFilterBean(2,"橙色",3,"orange",1))
            desktopColorList.add(WallpaperFilterBean(3,"红色",3,"red",1))
            desktopColorList.add(WallpaperFilterBean(4,"粉色",3,"pink",1))
            desktopColorList.add(WallpaperFilterBean(5,"紫色",3,"purple",1))
            desktopColorList.add(WallpaperFilterBean(6,"绿色",3,"green",1))
            desktopColorList.add(WallpaperFilterBean(7,"蓝色",3,"blue",1))
            desktopColorList.add(WallpaperFilterBean(8,"灰色",3,"gray",1))
            desktopColorList.add(WallpaperFilterBean(9,"黑色",3,"black",1))
            desktopColorList.add(WallpaperFilterBean(10,"炫彩",3,"colorful",1))
            desktopColorList.add(WallpaperFilterBean(11,"白色",3,"white",1))
        }

        return desktopColorList
    }

    fun getPhoneColor() : ArrayList<WallpaperFilterBean>{
        if (phoneColorList.size == 0) {
            phoneColorList.add(WallpaperFilterBean(0,"全部颜色",3,isSelect = true))
            phoneColorList.add(WallpaperFilterBean(1,"黄色",3,"yellow",1))
            phoneColorList.add(WallpaperFilterBean(2,"橙色",3,"orange",1))
            phoneColorList.add(WallpaperFilterBean(3,"红色",3,"red",1))
            phoneColorList.add(WallpaperFilterBean(4,"粉色",3,"pink",1))
            phoneColorList.add(WallpaperFilterBean(5,"紫色",3,"purple",1))
            phoneColorList.add(WallpaperFilterBean(6,"绿色",3,"green",1))
            phoneColorList.add(WallpaperFilterBean(7,"蓝色",3,"blue",1))
            phoneColorList.add(WallpaperFilterBean(8,"灰色",3,"gray",1))
            phoneColorList.add(WallpaperFilterBean(9,"黑色",3,"black",1))
            phoneColorList.add(WallpaperFilterBean(10,"炫彩",3,"colorful",1))
            phoneColorList.add(WallpaperFilterBean(11,"白色",3,"white",1))
        }

        return desktopColorList
    }
}