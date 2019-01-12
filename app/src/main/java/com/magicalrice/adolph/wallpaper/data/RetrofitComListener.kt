package com.magicalrice.adolph.wallpaper.data

interface RetrofitComListener<T> : RetrofitNextListener<T> {
    fun onComplete()
}