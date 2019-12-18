package app.sargis.khlopuzyan.lastfm.networking.callback

import okhttp3.ResponseBody

/**
 * Created by Sargis Khlopuzyan, on 12/16/2019.
 *
 * @author Sargis Khlopuzyan (sargis.khlopuzyan@gmail.com)
 */
sealed class Result<out T> {
    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val errorCode: Int, val responseBody: ResponseBody?) : Result<Nothing>()
    data class Failure(val error: Throwable?) : Result<Nothing>()
}