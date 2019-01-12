package com.magicalrice.adolph.wallpaper.data.service

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

/**
 * 壁纸分类(type)
 * 全部 0;游戏壁纸 191;卡通动漫 192;家居壁纸 193;军事壁纸 194;汽车壁纸 195;广告壁纸 196;设计创意 197;港台影视 198;欧美影视 199;
 * 日韩影视 200;大陆影视 201;港台明星 202;欧美明星 203;日韩明星 204;大陆明星 205;动物壁纸 206;高清壁纸 207;风景壁纸 208;
 * 植物壁纸 209;美女壁纸 2285;日历壁纸 2286;节日壁纸 2287;体育壁纸 2357;风格壁纸 2358;美食壁纸 2361;
 *
 * 壁纸尺寸(size)
 * 全部 0;1024*768 1;1280*800 2;1280*1024 3;1366*768 13;1440*900 4;1600*900 12;1600*1200 5;1680*1050 6;
 * 1920*1080 10;1920*1200 7;1920*1440 8;2560*1440 14;2560*1600 11;其他尺寸 9;
 *
 * 壁纸颜色(color)
 * 全部 0;黄色 1;橙色 2;红色 3;粉色 4;紫色 5;绿色 6;蓝色 7;灰色 8;黑色 9;炫彩 10;白色 11;
 */

interface WallpaperService {
    @GET("/wallpaper_{type}_{color}_{size}_{page}.html")
    fun getWallpaperDesktop(
        @Path("type") type: Int,
        @Path("color") color: Int,
        @Path("size") size: Int,
        @Path("page") page: Int
    ) : Observable<ResponseBody>

    @GET("/mobile_{type}_{color}_{size}_{page}.html")
    fun getWallpaperPhone(
            @Path("type") type: Int,
            @Path("color") color: Int,
            @Path("size") size: Int,
            @Path("page") page: Int
    ) : Observable<ResponseBody>

    @GET()
    fun getWallpaperDetail(
        @Url url: String
    ) : Observable<ResponseBody>
}