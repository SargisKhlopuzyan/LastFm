package app.sargis.khlopuzyan.lastfm.ui.artists_search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.sargis.khlopuzyan.lastfm.R
import app.sargis.khlopuzyan.lastfm.databinding.LayoutRecyclerViewItemArtistsSearchBinding
import app.sargis.khlopuzyan.lastfm.databinding.LayoutRecyclerViewItemArtistsSearchNetworkErrorBinding
import app.sargis.khlopuzyan.lastfm.databinding.LayoutRecyclerViewItemLoadingBinding
import app.sargis.khlopuzyan.lastfm.model.artists_search.Artist
import app.sargis.khlopuzyan.lastfm.ui.common.BindableAdapter
import app.sargis.khlopuzyan.lastfm.util.NetworkState

/**
 * Created by Sargis Khlopuzyan, on 12/18/2019.
 *
 * @author Sargis Khlopuzyan (sargis.khlopuzyan@gmail.com)
 */
class ArtistsSearchAdapter(
    val viewModel: ArtistsSearchViewModel
) : ListAdapter<Artist?, RecyclerView.ViewHolder>(DiffCallback()), BindableAdapter<List<Artist>> {

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {

            R.layout.layout_recycler_view_item_artists_search -> {

                val binding: LayoutRecyclerViewItemArtistsSearchBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.layout_recycler_view_item_artists_search,
                    parent, false
                )
                ArtistViewHolder(binding)
            }

            R.layout.layout_recycler_view_item_loading -> {
                val binding: LayoutRecyclerViewItemLoadingBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.layout_recycler_view_item_loading,
                    parent, false
                )
                LoadingViewHolder(binding)
            }

            R.layout.layout_recycler_view_item_artists_search_network_error -> {

                val binding: LayoutRecyclerViewItemArtistsSearchNetworkErrorBinding =
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.layout_recycler_view_item_artists_search_network_error,
                        parent, false
                    )
                NetworkErrorViewHolder(binding)
            }

            else -> throw IllegalArgumentException("Unknown view type $viewType")
        }
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

    override fun getItemId(position: Int) = position.toLong()


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (getItemViewType(position)) {

            R.layout.layout_recycler_view_item_artists_search -> {
                (holder as ArtistViewHolder).bindItem(getItem(position), viewModel)
            }

            R.layout.layout_recycler_view_item_loading -> {
                if (networkState == NetworkState.Loaded && viewModel.hasExtraRow()) {
                    viewModel.searchMoreArtists()
                }
            }

            R.layout.layout_recycler_view_item_artists_search_network_error -> {
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
                    R.layout.layout_recycler_view_item_artists_search
                }
            }

            is NetworkState.Failure -> {
                if (viewModel.hasExtraRow() && position == itemCount - 1) {
                    R.layout.layout_recycler_view_item_artists_search_network_error
                } else {
                    R.layout.layout_recycler_view_item_artists_search
                }
            }

            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun setItems(items: List<Artist>?) {
        if (items != null) {
            submitList(items)
        } else {
            submitList(listOf())
        }
    }

    override fun setNetworkState(newNetworkState: NetworkState?) {
        networkState = newNetworkState
        notifyItemChanged(0)
    }

    // ViewHolder

    class ArtistViewHolder(private val binding: LayoutRecyclerViewItemArtistsSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindItem(artist: Artist?, viewModel: ArtistsSearchViewModel) {
            binding.viewModel = viewModel
            binding.artist = artist
        }
    }

    class LoadingViewHolder(binding: LayoutRecyclerViewItemLoadingBinding) :
        RecyclerView.ViewHolder(binding.root)

    class NetworkErrorViewHolder(binding: LayoutRecyclerViewItemArtistsSearchNetworkErrorBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val binding: LayoutRecyclerViewItemArtistsSearchNetworkErrorBinding = binding

        fun bind(viewModel: ViewModel) {
            binding.viewModel = viewModel as ArtistsSearchViewModel
        }
    }

}

//Callback

class DiffCallback : DiffUtil.ItemCallback<Artist?>() {

    override fun areItemsTheSame(oldItem: Artist, newItem: Artist): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Artist, newItem: Artist): Boolean {
        return oldItem == newItem
    }

}