package app.sargis.khlopuzyan.lastfm.networking.helper

import app.sargis.khlopuzyan.lastfm.networking.callback.Result
import retrofit2.HttpException
import retrofit2.Response

/**
 * Created by Sargis Khlopuzyan, on 12/16/2019.
 *
 * @author Sargis Khlopuzyan (sargis.khlopuzyan@gmail.com)
 */
fun <T : Any> Response<T>.getResult(): Result<T> {
    return if (this.isSuccessful) {
        val body = this.body()
        if (body != null) {
            Result.Success(body)
        } else {
            Result.Error(this.code(), this.errorBody())
        }
    } else {
        Result.Failure(HttpException(this))
    }
}