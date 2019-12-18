package app.sargis.khlopuzyan.lastfm.ui.album_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import app.sargis.khlopuzyan.lastfm.R
import app.sargis.khlopuzyan.lastfm.databinding.FragmentAlbumDetailsBinding
import app.sargis.khlopuzyan.lastfm.model.top_albums.Album
import app.sargis.khlopuzyan.lastfm.ui.common.DaggerFragmentX
import javax.inject.Inject

class AlbumDetailsFragment : DaggerFragmentX() {

    @Inject
    lateinit var viewModel: AlbumDetailsViewModel

    private lateinit var binding: FragmentAlbumDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val album: Album? = arguments?.getParcelable(ARG_ALBUM)
        viewModel.setAlbum(album)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_album_details, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.viewModel = viewModel
    }

    companion object {

        const val ARG_ALBUM = "arg_album"
        fun newInstance(album: Album) = AlbumDetailsFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_ALBUM, album)
            }
        }
    }

}