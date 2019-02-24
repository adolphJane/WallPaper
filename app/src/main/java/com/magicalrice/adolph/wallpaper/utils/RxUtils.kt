package com.magicalrice.adolph.wallpaper.utils

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object RxUtils {
    fun <T> io_main() : ObservableTransformer<T,T>{
        return ObservableTransformer {
            it.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun doOnUIThread(task: UITask) {
        Observable.just(task)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{
                it.doOnUI()
            }
    }

    fun doOnIOThread(task: IOTask) {
        Observable.just(task)
            .observeOn(Schedulers.io())
            .subscribe{
                it.doOnIO()
            }
    }
}