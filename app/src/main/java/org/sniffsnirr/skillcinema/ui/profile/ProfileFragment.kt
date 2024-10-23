package org.sniffsnirr.skillcinema.ui.profile

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.sniffsnirr.skillcinema.MainActivity.Companion.FIRST_START
import org.sniffsnirr.skillcinema.MainActivity.Companion.REQUIRE_REFRESH_HOME_FRAGMENT
import org.sniffsnirr.skillcinema.R
import org.sniffsnirr.skillcinema.databinding.FragmentProfileBinding
import org.sniffsnirr.skillcinema.ui.collections.paging.presets.PagingCollectionFragment
import org.sniffsnirr.skillcinema.ui.home.HomeFragment.Companion.ID_MOVIE
import org.sniffsnirr.skillcinema.ui.home.adapter.MainAdapter
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import org.sniffsnirr.skillcinema.ui.onemovie.adapter.GalleryAdapter
import org.sniffsnirr.skillcinema.ui.profile.adapter.InterestedAdapter
import org.sniffsnirr.skillcinema.ui.profile.adapter.ViewedAdapter

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewedAdapter = ViewedAdapter({ onClearAllViewedClick() },{idMovie,position -> onMovieClick(idMovie,position)})

    private val interestedAdapter = InterestedAdapter({ onClearAllInterestedClick() },{idMovie,position -> onMovieClick(idMovie,position)})

    var possiblyEditablePosition=0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModel.loadViewedMovies()
        viewModel.loadInterestedMovies()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewedRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.viewedRv.setHasFixedSize(true)
        binding.viewedRv.adapter = viewedAdapter

        viewLifecycleOwner.lifecycleScope.launch {// загрузка просмотренных
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewedMovies.collect {
                    viewedAdapter.setData(it)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {// загрузка просмотренных
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.countViewedMovies.collect {
                    binding.allViewedMoviesButton.text=it.toString()
                }
            }
        }

        binding.interestedRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.interestedRv.setHasFixedSize(true)
        binding.interestedRv.adapter = interestedAdapter

        viewLifecycleOwner.lifecycleScope.launch {// загрузка попавших в интересные
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.interestedMovies.collect {
                    interestedAdapter.setData(it)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {// загрузка количества попавших в интересные
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.countInterestedMovies.collect {
                    binding.allInterestedButton.text=it.toString()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onMovieClick(idMovie: Int?,position:Int) {
        val bundle = Bundle()
        if(idMovie!=null){
            bundle.putInt(ID_MOVIE, idMovie)
            findNavController().navigate(
                R.id.action_navigation_profile_to_oneMovieFragment,
                bundle
            )
        }
        possiblyEditablePosition=position
    }

     override fun onResume() {
        super.onResume()
        setFragmentResultListener(PagingCollectionFragment.RV_ITEM_HAS_BEEN_CHANGED_REQUEST_KEY) { REQUEST_KEY, bundle ->
                if (bundle.getBoolean(PagingCollectionFragment.RV_ITEM_HAS_BEEN_CHANGED_BUNDLE_KEY) != null) {
                    if (bundle.getBoolean(PagingCollectionFragment.RV_ITEM_HAS_BEEN_CHANGED_BUNDLE_KEY)) {
                        Log.d("Update!@#$", "Обновляю RV на ProfileFragment из OneMovieFragment")
                        viewedAdapter.deleteFromRV(possiblyEditablePosition)
                   }
                }
            }
    }

    fun onClearAllViewedClick(){
        viewModel.clearViewedCollection()
        setFragmentResult(//сигнал на предыдущий фрагмент - нужно обновить rv
            PagingCollectionFragment.RV_ITEM_HAS_BEEN_CHANGED_REQUEST_KEY,
            bundleOf(PagingCollectionFragment.RV_ITEM_HAS_BEEN_CHANGED_BUNDLE_KEY to true)
        )
        setFragmentResult(//сигнал на home фрагмент - нужно обновить rv
           RV_ITEM_HAS_BEEN_CHANGED_IN_PRIFILE_FRAGMENT_REQUEST_KEY,
            bundleOf(RV_ITEM_HAS_BEEN_CHANGED_IN_PRIFILE_FRAGMENT_BUNDLE_KEY to true)
        )
    }

    fun onClearAllInterestedClick(){
        viewModel.clearInterstedCollection()
    }

    companion object {
        const val LIMIT_FOR_RV = 8
        const val ID_FAVORITE_COLLECTION = 1L
        const val ID_WANT_TO_SEE_COLLECTION = 2L
        const val ID_VIEWED_COLLECTION = 3L
        const val ID_INTERESTED_COLLECTION = 4L

        const val RV_ITEM_HAS_BEEN_CHANGED_IN_PRIFILE_FRAGMENT_REQUEST_KEY="PRIFILE_FRAGMENT"
        const val RV_ITEM_HAS_BEEN_CHANGED_IN_PRIFILE_FRAGMENT_BUNDLE_KEY="PRIFILE_FRAGMENT"
    }
}