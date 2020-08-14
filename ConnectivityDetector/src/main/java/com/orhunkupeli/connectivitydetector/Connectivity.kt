package com.orhunkupeli.connectivitydetector


import android.net.NetworkCapabilities
import android.net.ConnectivityManager.NetworkCallback
import androidx.annotation.RequiresApi
import android.net.Network
import android.os.Build
import android.net.ConnectivityManager

@RequiresApi(Build.VERSION_CODES.N)
class Connectivity(private val mConnectivityManager: ConnectivityManager) : ConnectivityBase() {

    private var mIsSubscribed: Boolean = false

    private val mNetworkCallback = NetworkListener()

    override fun addListener(listener: NetworkStateListener) {
        super.addListener(listener)
        isSubscribedCheck()
    }

    override fun removeListener(listener: NetworkStateListener) {
        super.removeListener(listener)
        isSubscribedCheck()
    }

    private fun isSubscribedCheck() {
        if (!mIsSubscribed) {
            register()
            mIsSubscribed = true
        } else if (mIsSubscribed) {
            unregister()
            mIsSubscribed = false
        }
    }

    private inner class NetworkListener : NetworkCallback() {
        override fun onCapabilitiesChanged(network: Network, capabilities: NetworkCapabilities) {
            notifyNetworkStateChanged(NetworkState.ConnectedState.Connected(capabilities))
        }

        override fun onLost(network: Network) {
            notifyNetworkStateChanged(NetworkState.NoConnection)
        }
    }

    override fun register() {
        mConnectivityManager.registerDefaultNetworkCallback(mNetworkCallback)
    }

    override fun unregister() {
        mConnectivityManager.unregisterNetworkCallback(mNetworkCallback)
    }
}