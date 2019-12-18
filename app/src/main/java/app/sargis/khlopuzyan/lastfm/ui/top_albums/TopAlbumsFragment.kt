package app.sargis.khlopuzyan.lastfm.ui.top_albums

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.commit
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager

import app.sargis.khlopuzyan.lastfm.R
import app.sargis.khlopuzyan.lastfm.databinding.FragmentTopAlbumsBinding
import app.sargis.khlopuzyan.lastfm.model.top_albums.Album
import app.sargis.khlopuzyan.lastfm.ui.album_details.AlbumDetailsFragment
import app.sargis.khlopuzyan.lastfm.ui.common.DaggerFragmentX
import javax.inject.Inject

class TopAlbumsFragment : DaggerFragmentX() {

    @Inject
    lateinit var viewModel: TopAlbumsViewModel

    private lateinit var binding: FragmentTopAlbumsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val artistName: String? = arguments?.getString(ARG_ARTIST_NAME)
        viewModel.setArtist(artistName)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_top_albums, container, false)
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
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.hasFixedSize()

        val adapter = TopAlbumsAdapter(
            viewModel
        )
        adapter.setHasStableIds(true)
        binding.recyclerView.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.openAlbumDetailLiveData.observe(this) {
            openAlbumDetailsFragment(it)
        }

        viewModel.showToastLiveData.observe(this) {
            Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
        }
    }

    private fun openAlbumDetailsFragment(
        album: Album
    ) {
        activity?.supportFragmentManager?.commit {
            replace(
                android.R.id.content,
                AlbumDetailsFragment.newInstance(album),
                "fragment_album_details"
            )
            addToBackStack("detail")
        }
    }

    companion object {
        private const val ARG_ARTIST_NAME = "arg_artist_name"

        fun newInstance(artistName: String) = TopAlbumsFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_ARTIST_NAME, artistName)
            }
        }
    }

}