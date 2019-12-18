package app.sargis.khlopuzyan.lastfm.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import app.sargis.khlopuzyan.lastfm.ui.cached_albums.CachedAlbumsFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(
                    android.R.id.content,
                    CachedAlbumsFragment.newInstance(),
                    "fragment_cached_albums"
                )
            }
        }
    }
}
