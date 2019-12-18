package app.sargis.khlopuzyan.lastfm.ui.top_albums

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.sargis.khlopuzyan.lastfm.R
import app.sargis.khlopuzyan.lastfm.databinding.LayoutRecyclerViewItemLoadingBinding
import app.sargis.khlopuzyan.lastfm.databinding.LayoutRecyclerViewItemTopAlbumsBinding
import app.sargis.khlopuzyan.lastfm.databinding.LayoutRecyclerViewItemTopAlbumsNetworkErrorBinding
import app.sargis.khlopuzyan.lastfm.model.top_albums.Album
import app.sargis.khlopuzyan.lastfm.ui.common.BindableAdapter
import app.sargis.khlopuzyan.lastfm.util.NetworkState

/**
 * Created by Sargis Khlopuzyan, on 12/18/2019.
 *
 * @author Sargis Khlopuzyan (sargis.khlopuzyan@gmail.com)
 */
class TopAlbumsAdapter(
    val viewModel: TopAlbumsViewModel
) : ListAdapter<Album?, RecyclerView.ViewHolder>(DiffCallback()), BindableAdapter<List<Album>> {

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {

            R.layout.layout_recycler_view_item_top_albums -> {

                val binding: LayoutRecyclerViewItemTopAlbumsBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.layout_recycler_view_item_top_albums,
                    parent, false
                )
                TopAlbumsViewHolder(binding)
            }

            R.layout.layout_recycler_view_item_loading -> {
                val binding: LayoutRecyclerViewItemLoadingBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.layout_recycler_view_item_loading,
                    parent, false
                )
                LoadingViewHolder(binding)
            }

            R.layout.layout_recycler_view_item_top_albums_network_error -> {

                val binding: LayoutRecyclerViewItemTopAlbumsNetworkErrorBinding =
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.layout_recycler_view_item_top_albums_network_error,
                        parent, false
                    )
                NetworkErrorViewHolder(binding)
            }

            else -> throw IllegalArgumentException("Unknown view type $viewType")
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {

        if (networkState == null) {
            return 0
        }

        return if (viewModel.hasExtraRow()) {
            super.getItemCount() + 1
        } else {
            super.getItemCount()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {

            R.layout.layout_recycler_view_item_top_albums -> {
                (holder as TopAlbumsViewHolder).bindItem(getItem(position), viewModel)
            }

            R.layout.layout_recycler_view_item_loading -> {
                if (networkState == NetworkState.Loaded && viewModel.hasExtraRow()) {
                    viewModel.searchMoreAlbums()
                }
            }

            R.layout.layout_recycler_view_item_top_albums_network_error -> {
                (holder as NetworkErrorViewHolder).bind(viewModel)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {

        return when (networkState) {

            is NetworkState.Loaded, is NetworkState.Loading -> {
                if (viewModel.hasExtraRow() && position == itemCount - 1) {
                    R.layout.layout_recycler_view_item_loading
                } else {
                    R.layout.layout_recycler_view_item_top_albums
                }
            }

            is NetworkState.Failure -> {
                if (viewModel.hasExtraRow() && position == itemCount - 1) {
                    R.layout.layout_recycler_view_item_top_albums_network_error
                } else {
                    R.layout.layout_recycler_view_item_top_albums
                }
            }

            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun setItems(items: List<Album>?) {
        if (items != null) {
            submitList(items)
        } else {
            submitList(listOf())
        }
    }

    override fun setNetworkState(newNetworkState: NetworkState?) {
        Log.e("LOG_TAG", "setNetworkState -> $newNetworkState")
        networkState = newNetworkState
        //TODO
        notifyDataSetChanged()


//        if (newNetworkState == NetworkState.Loaded) {
//            if (hadExtraRow) {
//                notifyItemRemoved(prevItemCount)
//            } else {
//                notifyItemInserted(super.getItemCount())
//            }
//        } else if (hasExtraRow && previousState != newNetworkState) {
//            notifyItemChanged(itemCount - 1)
//        }
    }

    class TopAlbumsViewHolder(private val binding: LayoutRecyclerViewItemTopAlbumsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindItem(album: Album?, viewModel: TopAlbumsViewModel) {
            binding.viewModel = viewModel
            binding.album = album
        }
    }

    class LoadingViewHolder(binding: LayoutRecyclerViewItemLoadingBinding) :
        RecyclerView.ViewHolder(binding.root)

    class NetworkErrorViewHolder(binding: LayoutRecyclerViewItemTopAlbumsNetworkErrorBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val binding: LayoutRecyclerViewItemTopAlbumsNetworkErrorBinding = binding

        fun bind(viewModel: TopAlbumsViewModel) {
            binding.viewModel = viewModel
        }
    }
}

//Callback

class DiffCallback : DiffUtil.ItemCallback<Album?>() {

    override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
        return oldItem.name == newItem.name && oldItem.albumCacheState == newItem.albumCacheState
    }

    override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
        return oldItem == newItem
    }

}