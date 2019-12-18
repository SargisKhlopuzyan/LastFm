package app.sargis.khlopuzyan.lastfm.ui.cached_albums

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.sargis.khlopuzyan.lastfm.R
import app.sargis.khlopuzyan.lastfm.databinding.LayoutRecyclerViewItemCachedAlbumsBinding
import app.sargis.khlopuzyan.lastfm.model.top_albums.Album
import app.sargis.khlopuzyan.lastfm.ui.common.BindableAdapter
import app.sargis.khlopuzyan.lastfm.util.NetworkState

/**
 * Created by Sargis Khlopuzyan, on 12/18/2019.
 *
 * @author Sargis Khlopuzyan (sargis.khlopuzyan@gmail.com)
 */
class CachedAlbumsAdapter(
    val viewModel: CachedAlbumsViewModel
) : RecyclerView.Adapter<CachedAlbumsAdapter.ViewHolder>(), BindableAdapter<List<Album>> {

    private var storedAlbums = mutableListOf<Album>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: LayoutRecyclerViewItemCachedAlbumsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.layout_recycler_view_item_cached_albums,
            parent, false
        )
        return ViewHolder(
            binding
        )
    }

    override fun getItemCount(): Int {
        return storedAlbums.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(storedAlbums[position], viewModel)
    }

    override fun setItems(items: List<Album>?) {
        storedAlbums.clear()
        items?.let {
            storedAlbums.addAll(items)
        }
        notifyDataSetChanged()
    }

    override fun setNetworkState(networkState: NetworkState?) {

    }

    class ViewHolder(binding: LayoutRecyclerViewItemCachedAlbumsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        var binding: LayoutRecyclerViewItemCachedAlbumsBinding = binding

        fun bindData(album: Album, viewModel: CachedAlbumsViewModel) {
            binding.album = album
            binding.viewModel = viewModel
            binding.textViewAlbum.text = album.name
            binding.textViewArtist.text = album.name
        }

    }

}