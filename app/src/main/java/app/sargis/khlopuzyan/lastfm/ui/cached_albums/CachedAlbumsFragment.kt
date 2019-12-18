package app.sargis.khlopuzyan.lastfm.ui.cached_albums

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.commit
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import app.sargis.khlopuzyan.lastfm.R
import app.sargis.khlopuzyan.lastfm.databinding.FragmentCachedAlbumsBinding
import app.sargis.khlopuzyan.lastfm.model.top_albums.Album
import app.sargis.khlopuzyan.lastfm.ui.album_details.AlbumDetailsFragment
import app.sargis.khlopuzyan.lastfm.ui.artists_search.ArtistsSearchFragment
import app.sargis.khlopuzyan.lastfm.ui.common.DaggerFragmentX
import javax.inject.Inject

class CachedAlbumsFragment : DaggerFragmentX() {

    @Inject
    lateinit var viewModel: CachedAlbumsViewModel

    private lateinit var binding: FragmentCachedAlbumsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_cached_albums, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.viewModel = viewModel

        setupToolbar()
        setupRecyclerView()
        setupObservers()
    }

    private fun setupToolbar() {
        binding.toolbar.title = ""
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(context)

        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.hasFixedSize()
        val adapter = CachedAlbumsAdapter(
            viewModel
        )
        binding.recyclerView.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.openSearchLiveData.observe(this) {
            openArtistsSearchScreen()
        }

        viewModel.openAlbumDetailLiveData.observe(this) {
            openAlbumDetailsScreen(it)
        }
    }

    private fun openAlbumDetailsScreen(
        album: Album
    ) {
        activity?.supportFragmentManager?.commit {
            replace(
                android.R.id.content,
                AlbumDetailsFragment.newInstance(album),
                "fragment_album_details"
            )
            addToBackStack("album_details")
        }
    }

    private fun openArtistsSearchScreen() {
        activity?.supportFragmentManager?.commit {
            replace(
                android.R.id.content,
                ArtistsSearchFragment.newInstance(),
                "artists_search_fragment"
            ).addToBackStack("artists_search")
        }
    }

    companion object {
        fun newInstance() = CachedAlbumsFragment()
    }

}