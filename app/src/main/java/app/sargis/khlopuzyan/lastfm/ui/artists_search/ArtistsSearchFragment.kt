package app.sargis.khlopuzyan.lastfm.ui.artists_search

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
import app.sargis.khlopuzyan.lastfm.databinding.FragmentArtistsSearchBinding
import app.sargis.khlopuzyan.lastfm.ui.common.DaggerFragmentX
import app.sargis.khlopuzyan.lastfm.ui.top_albums.TopAlbumsFragment
import kotlinx.android.synthetic.main.fragment_cached_albums.*
import javax.inject.Inject

class ArtistsSearchFragment : DaggerFragmentX() {

    @Inject
    lateinit var viewModelArtists: ArtistsSearchViewModel

    private lateinit var binding: FragmentArtistsSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_artists_search, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.viewModel = viewModelArtists

        setupToolbar()
        setupRecyclerView()
        setupObservers()
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        binding.recyclerView.layoutManager = layoutManager
        recyclerView.hasFixedSize()

        val adapter = ArtistsSearchAdapter(
            viewModelArtists
        )
        adapter.setHasStableIds(true)
        binding.recyclerView.adapter = adapter
    }

    private fun setupObservers() {
        viewModelArtists.openTopAlbumsLiveData.observe(this) {
            openTopAlbumsFragment(it)
        }
    }

    private fun openTopAlbumsFragment(
        artistName: String
    ) {
        activity?.supportFragmentManager?.commit {
            replace(
                android.R.id.content,
                TopAlbumsFragment.newInstance(artistName),
                "fragment_top_albums"
            )
            addToBackStack("top_albums")
        }
    }

    companion object {
        fun newInstance() = ArtistsSearchFragment()
    }

}