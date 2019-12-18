package app.sargis.khlopuzyan.lastfm.ui.common

import app.sargis.khlopuzyan.lastfm.util.NetworkState

/**
 * Created by Sargis Khlopuzyan, on 12/18/2019.
 *
 * @author Sargis Khlopuzyan (sargis.khlopuzyan@gmail.com)
 */
interface BindableAdapter<T> {
    fun setItems(items: T)
    fun setNetworkState(networkState: NetworkState?)
}