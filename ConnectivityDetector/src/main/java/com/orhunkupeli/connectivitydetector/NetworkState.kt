package com.orhunkupeli.connectivitydetector

import android.net.NetworkCapabilities
import android.net.NetworkInfo

sealed class NetworkState {
    object NoConnection : NetworkState()

    sealed class ConnectedState(val isConnected: Boolean) : NetworkState() {

        data class Connected(val capabilities: NetworkCapabilities) : ConnectedState(
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        )

        data class ConnectedLegacy(val networkInfo: NetworkInfo) : ConnectedState(
            networkInfo.isConnectedOrConnecting
        )
    }
}