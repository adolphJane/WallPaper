package com.magicalrice.adolph.wallpaper.data

interface RetrofitNextListener<T> {
    fun onNext(t: T)
}