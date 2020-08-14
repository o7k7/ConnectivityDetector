package com.orhunkupeli.connectivitydetector

import android.os.Handler
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.os.Looper

abstract class ConnectivityBase : BaseObservable<ConnectivityBase.NetworkStateListener>() {

    private val mHandler = Handler(Looper.getMainLooper()) // UI safe

    protected abstract fun register()

    protected abstract fun unregister()

    interface NetworkStateListener {
        fun onStateChange(state: NetworkState)
    }

    companion object {
        fun getConnectivityDetector(context: Context): ConnectivityBase {
            (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).apply {
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Connectivity(this)
                } else {
                    BequestConnectivity(context, this)
                }
            }
        }
    }

    open fun addListener(listener: NetworkStateListener) {
        super.registerListener(listener)
    }

    open fun removeListener(listener: NetworkStateListener) {
        super.unregisterListener(listener)
    }

    protected fun notifyNetworkStateChanged(state: NetworkState) {
        mHandler.post {
            super.getListeners().forEach {
                it.onStateChange(state)
            }
        }
    }
}