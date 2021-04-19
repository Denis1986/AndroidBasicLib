package io.github.denis1986.android.base.network.data

/**
 * Created by Denis Druzhinin on 08.01.2021.
 */
enum class NetworkState(val hasConnection: Boolean) {
    Wifi(true),
    CellularOnly(true),
    NotConnected(false);
}