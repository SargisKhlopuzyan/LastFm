package app.sargis.khlopuzyan.lastfm.ui.artists_search

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.commit
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import app.sargis.khlopuzyan.lastfm.R
import app.sargis.khlopuzyan.lastfm.databinding.FragmentArtistsSearchBinding
import app.sargis.khlopuzyan.lastfm.ui.common.DaggerFragmentX
import app.sargis.khlopuzyan.lastfm.ui.top_albums.TopAlbumsFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_cached_albums.*
import javax.inject.Inject


class ArtistsSearchFragment : DaggerFragmentX() {

    @Inject
    lateinit var viewModel: ArtistsSearchViewModel

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
        binding.viewModel = viewModel

        setupToolbar()
        setupRecyclerView()
        setupSearchView()
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
            viewModel
        )
        adapter.setHasStableIds(true)
        binding.recyclerView.adapter = adapter
    }

    private fun setupSearchView() {
        val requestFocus: Boolean = arguments?.getBoolean(ARG_REQUEST_FOCUS, false) ?: false
        if (requestFocus) {
            binding.searchView.isIconified = false
            arguments?.putBoolean(ARG_REQUEST_FOCUS, false)
        }
    }

    private fun setupObservers() {
        viewModel.openTopAlbumsLiveData.observe(this) {
            openTopAlbumsFragment(it)
        }

        viewModel.showToastLiveData.observe(this) {
            Snackbar.make(binding.toolbar, "$it", Snackbar.LENGTH_SHORT)
                .show()
        }

        viewModel.hideKeyboardLiveData.observe(this) {
            hideKeyboard(it)
        }
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager =
            activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
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

        private const val ARG_REQUEST_FOCUS = "arg_request_focus"
        fun newInstance(requestFocus: Boolean = false) = ArtistsSearchFragment().apply {
            arguments = Bundle().apply {
                putBoolean(ARG_REQUEST_FOCUS, requestFocus)
            }
        }
    }

}