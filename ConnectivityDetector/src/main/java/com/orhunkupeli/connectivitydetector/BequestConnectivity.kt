package com.orhunkupeli.connectivitydetector

import android.content.BroadcastReceiver
import android.net.ConnectivityManager.CONNECTIVITY_ACTION
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.NetworkInfo
import android.net.ConnectivityManager
import android.net.ConnectivityManager.EXTRA_NETWORK_INFO

@Suppress("DEPRECATION")
class BequestConnectivity(
    private val mContext: Context,
    private val mConnectivityManager: ConnectivityManager
) : ConnectivityBase() {

    private val mReceiver = NetworkListener()

    override fun register() {
        mContext.registerReceiver(mReceiver, IntentFilter(CONNECTIVITY_ACTION))
    }

    override fun unregister() {
        mContext.unregisterReceiver(mReceiver)
    }

    private inner class NetworkListener : BroadcastReceiver() {
        override fun onReceive(ctx: Context, intent: Intent) {
            val fallbackNetworkInfo: NetworkInfo? = intent.getParcelableExtra(EXTRA_NETWORK_INFO)
            val networkInfo = mConnectivityManager.activeNetworkInfo
            val state: NetworkState = if (
                networkInfo?.isConnectedOrConnecting == true
            ) {
                NetworkState.ConnectedState.ConnectedLegacy(networkInfo)
            } else if (
                networkInfo != null &&
                fallbackNetworkInfo != null &&
                networkInfo.isConnectedOrConnecting != fallbackNetworkInfo.isConnectedOrConnecting
            ) {
                NetworkState.ConnectedState.ConnectedLegacy(fallbackNetworkInfo)
            } else {
                val state = networkInfo ?: fallbackNetworkInfo
                state?.let {
                    NetworkState.ConnectedState.ConnectedLegacy(state)
                } ?: kotlin.run {
                    NetworkState.NoConnection
                }
            }
            notifyNetworkStateChanged(state)
        }
    }
}