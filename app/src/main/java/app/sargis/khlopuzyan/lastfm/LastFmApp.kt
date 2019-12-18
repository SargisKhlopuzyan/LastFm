package app.sargis.khlopuzyan.lastfm

import app.sargis.khlopuzyan.lastfm.di.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import timber.log.Timber

/**
 * Created by Sargis Khlopuzyan, on 12/16/2019.
 *
 * @author Sargis Khlopuzyan (sargis.khlopuzyan@gmail.com)
 */
class LastFmApp : DaggerApplication() {

    init {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> = DaggerAppComponent
        .factory()
        .create(this)

}