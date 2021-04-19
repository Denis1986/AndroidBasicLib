package io.github.denis1986.android.base.network

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.core.content.ContextCompat
import io.github.denis1986.android.base.mvvm.MutableLiveDataWrapper
import io.github.denis1986.android.base.network.data.NetworkState

/** Note: this class can work only if app has ACCESS_NETWORK_STATE permission, otherwise [isEnabled] property will be false.
 * Created by Denis Druzhinin on 17.02.2020.
 */
class NetworkStateManager constructor(context: Context) {
    @Suppress("RemoveExplicitTypeArguments")
    val networkState = MutableLiveDataWrapper<NetworkState>()
    private val availableCellularNetworkSet = HashSet<Long>()
    private val availableWifiNetworkSet = HashSet<Long>()

    /** Is false if app has no ACCESS_NETWORK_STATE permission.
     */
    var isEnabled: Boolean = false
        private set

    init {
        isEnabled = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED

        if (isEnabled) {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            addNetworkStateListener(connectivityManager, NetworkCapabilities.TRANSPORT_WIFI, availableWifiNetworkSet)
            addNetworkStateListener(connectivityManager, NetworkCapabilities.TRANSPORT_CELLULAR, availableCellularNetworkSet)
        }
    }

    @SuppressLint("MissingPermission")
    private fun addNetworkStateListener(connectivityManager: ConnectivityManager, transport: Int, networkSetToUpdate: HashSet<Long>) {
        val networkRequest = NetworkRequest.Builder()
            .addTransportType(transport)
            .build()

        connectivityManager.registerNetworkCallback(
            networkRequest,
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    networkSetToUpdate.add(network.networkHandle)
                    updateConnectionAvailableState()
                }

                override fun onLost(network: Network) {
                    networkSetToUpdate.remove(network.networkHandle)
                    updateConnectionAvailableState()
                }
            }
        )
    }

    private fun updateConnectionAvailableState() {
        networkState.asMutable().postValue(when {
            availableWifiNetworkSet.size > 0 -> NetworkState.Wifi
            availableCellularNetworkSet.size > 0 -> NetworkState.CellularOnly
            else -> NetworkState.NotConnected
        })
    }
}