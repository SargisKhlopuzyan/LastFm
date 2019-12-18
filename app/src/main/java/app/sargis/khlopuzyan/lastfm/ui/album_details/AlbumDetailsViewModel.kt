package app.sargis.khlopuzyan.lastfm.ui.album_details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.sargis.khlopuzyan.lastfm.database.DatabaseManager
import app.sargis.khlopuzyan.lastfm.model.top_albums.Album
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlbumDetailsViewModel constructor(
    private val databaseManager: DatabaseManager
) : ViewModel() {

    var albumLiveData = MutableLiveData<Album>()

    fun setAlbum(album: Album?) {
        viewModelScope.launch(Dispatchers.Main) {
            album?.name?.let {
                albumLiveData.value = album
            }
        }
    }
}
