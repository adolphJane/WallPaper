package com.magicalrice.adolph.wallpaper.data

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import com.magicalrice.adolph.wallpaper.utils.ToastUtils
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


class NetSubscriber<T> : Observer<T> {
    private val TAG = "NetSubscriber"
    val REQUESTINVALID = 400
    val UNAUTHORIZED = 401
    val FORBIDDEN = 403
    val NOT_FOUND = 404
    val REQUEST_TIMEOUT = 408
    val INTERNAL_SERVER_ERROR = 500
    val BAD_GATEWAY = 502
    val SERVICE_UNAVAILABLE = 503
    val GATEWAY_TIMEOUT = 504
    private var mListener: RetrofitNextListener<T>
    private var mContext: Context
    private var refreshLayout: SmartRefreshLayout? = null
    private var loadingDialog: AlertDialog? = null

    constructor(
        refreshLayout: SmartRefreshLayout?,
        listener: RetrofitNextListener<T>,
        context: Context
    ) {
        this.refreshLayout = refreshLayout
        this.mListener = listener
        this.mContext = context
    }

    constructor(
        loadingDialog: AlertDialog?,
        listener: RetrofitNextListener<T>,
        context: Context
    ) {
        this.loadingDialog = loadingDialog
        this.mListener = listener
        this.mContext = context
    }

    constructor(listener: RetrofitNextListener<T>, context: Context) {
        this.mListener = listener
        this.mContext = context
    }


    override fun onComplete() {
        refreshLayout?.finishRefresh()

        loadingDialog?.let {
            it.dismiss()
        }
        if (mListener is RetrofitComListener) {
            (mListener as RetrofitComListener<T>).onComplete()
        }
    }

    override fun onNext(t: T) {
        refreshLayout?.finishRefresh()

        loadingDialog?.let {
            it.dismiss()
        }
        mListener.onNext(t)
    }

    override fun onError(p0: Throwable) {
        refreshLayout?.finishRefresh()
        loadingDialog?.let {
            it.dismiss()
        }
        if (mListener is RetrofitErrorListener) {
            (mListener as RetrofitErrorListener<T>).onError(p0)
        }
        when (p0) {
            is HttpException -> when (p0.response().code()) {
                INTERNAL_SERVER_ERROR -> {
                    val error = p0.response().errorBody()
                    error?.let {
//                        val resultBean: NetErrorBean? = Gson().fromJson<NetErrorBean>(
//                            String(it.bytes()),
//                            NetErrorBean::class.java
//                        )
                        showToastError(it.string())
                    }
                }

                REQUESTINVALID -> {
                    showToastError("请求无效")
//                    val error = p0.response().errorBody()
//                    error?.let {
//                        val resultBean = Gson().fromJson<NetErrorBean>(
//                            String(it.bytes()),
//                            NetErrorBean::class.java
//                        )
//                        showToastError(resultBean.message)
//                    }
                }

                UNAUTHORIZED -> {
                    //Token过期
                    showToastError("登录状态已失效，请重新登录！")
                }

                FORBIDDEN -> {
                    //请求被拒绝
                    showToastError("服务器拒绝请求")
                }

                NOT_FOUND -> {
                    //未找到资源
                    showToastError("服务器找不到请求的网页")
                }

                REQUEST_TIMEOUT -> {
                    showToastError("请求超时")
                }

                GATEWAY_TIMEOUT -> {
                    showToastError("网关超时")
                }

                SERVICE_UNAVAILABLE -> {
                    showToastError("服务器维护")
                }

                BAD_GATEWAY -> {
                    showToastError("服务器开小差了哦,请稍后再试")
                }

                else -> {
                    showToastError("服务器开小差了哦，请稍后重试")
                }
            }
            is ConnectException -> showToastError("网络请求失败，请稍后重试")
            is UnknownHostException -> showToastError("网络不可用")
            is SocketTimeoutException -> showToastError("网络请求超时")
            else -> showToastError(p0.message)
        }
    }

    override fun onSubscribe(p0: Disposable) {
        if (mListener is RetrofitDisposeListener) {
            (mListener as RetrofitDisposeListener).onDisposable(p0)
        }
    }

    private fun showToastError(content: String?) {
        if (content?.isEmpty() == false) {
            ToastUtils.showToast(mContext, content)
        }
    }


}