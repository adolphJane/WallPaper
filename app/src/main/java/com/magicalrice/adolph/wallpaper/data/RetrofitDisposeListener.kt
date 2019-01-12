package com.magicalrice.adolph.wallpaper.data

import io.reactivex.disposables.Disposable

interface RetrofitDisposeListener<T> : RetrofitNextListener<T>{
    fun onDisposable(dispose: Disposable)
}