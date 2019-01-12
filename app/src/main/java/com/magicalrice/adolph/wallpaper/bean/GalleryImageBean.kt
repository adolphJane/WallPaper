package com.magicalrice.adolph.wallpaper.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GalleryImageBean (
    var imgSrc: String,
    var width: Int,
    var height: Int
): Parcelable