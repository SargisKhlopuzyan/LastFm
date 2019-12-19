package app.sargis.khlopuzyan.lastfm.util

/**
 * Created by Sargis Khlopuzyan, on 12/16/2019.
 *
 * @author Sargis Khlopuzyan (sargis.khlopuzyan@gmail.com)
 */
sealed class DataLoadingState {
    object Loading : DataLoadingState()
    object Loaded : DataLoadingState()
    class Failure(val throwable: Throwable?) : DataLoadingState()
}