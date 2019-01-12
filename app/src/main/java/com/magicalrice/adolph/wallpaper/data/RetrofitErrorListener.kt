package com.magicalrice.adolph.wallpaper.data

interface RetrofitErrorListener<T> : RetrofitNextListener<T>{
    fun onError(t: Throwable)
}