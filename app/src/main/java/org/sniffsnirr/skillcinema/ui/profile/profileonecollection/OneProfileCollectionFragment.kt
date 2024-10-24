package org.sniffsnirr.skillcinema.ui.profile.profileonecollection

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.sniffsnirr.skillcinema.MainActivity
import org.sniffsnirr.skillcinema.R
import org.sniffsnirr.skillcinema.databinding.FragmentCollectionBinding
import org.sniffsnirr.skillcinema.ui.collections.paging.presets.PagingCollectionFragment.Companion.RV_ITEM_HAS_BEEN_CHANGED_BUNDLE_KEY
import org.sniffsnirr.skillcinema.ui.collections.paging.presets.PagingCollectionFragment.Companion.RV_ITEM_HAS_BEEN_CHANGED_REQUEST_KEY
import org.sniffsnirr.skillcinema.ui.home.HomeFragment
import org.sniffsnirr.skillcinema.ui.home.HomeFragment.Companion.ID_MOVIE
import org.sniffsnirr.skillcinema.ui.profile.ProfileFragment

// фрагмент для отображения одной коллеции из профайла
@AndroidEntryPoint
class OneProfileCollectionFragment : Fragment() {

    private val viewModel: OneProfileCollectionViewModel by viewModels()
    var _binding: FragmentCollectionBinding? = null
    val binding get() = _binding!!
    val adapter = OneProfileCollectionAdapter(emptyList()) { idMovie, position ->
        onMovieClick(idMovie,position)
    }
    var collectionName=""
    var collectionId=0
    var possiblyEditablePosition=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).showActionBar()
        collectionName = arguments?.getCharSequence(ProfileFragment.NAME_COLLECTION).toString()
        collectionId = arguments?.getInt(ProfileFragment.ID_COLLECTION)?:0
         viewModel.loadMoviesInCollection(collectionId.toLong())
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setActionBarTitle(collectionName)

        setFragmentResultListener(RV_ITEM_HAS_BEEN_CHANGED_REQUEST_KEY) { RV_ITEM_HAS_BEEN_CHANGED_REQUEST_KEY, bundle ->
            if (bundle.getBoolean(RV_ITEM_HAS_BEEN_CHANGED_BUNDLE_KEY) != null) {
                if (bundle.getBoolean(RV_ITEM_HAS_BEEN_CHANGED_BUNDLE_KEY)) {
                    adapter.updateMovieRVModel(possiblyEditablePosition)
// передаю сигнал об изменении выше
                    setFragmentResult(
                        HomeFragment.RV_ITEM_HAS_BEEN_CHANGED_REQUEST_KEY,
                        bundleOf(HomeFragment.RV_ITEM_HAS_BEEN_CHANGED_BUNDLE_KEY to true)
                    )
                    Log.d("Update", "Update_DONE!!!")
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.movieCollectionRv.adapter = adapter
        binding.movieCollectionRv.setHasFixedSize(true)

        viewModel.moviesInCollection.onEach {
            if (!it.isNullOrEmpty()) {
                adapter.setMovieModelList(it)
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).hideActionBar()
    }

    override fun onStop() {
        super.onStop()
        (activity as MainActivity).setActionBarTitle("")
    }

    private fun onMovieClick(idMovie: Int?,position:Int) {
        val bundle = Bundle()
        if (idMovie != null) {
            bundle.putInt(ID_MOVIE, idMovie)
            findNavController().navigate(
                R.id.action_collectionFragment_to_oneMovieFragment,
                bundle
            )
        }
        possiblyEditablePosition=position
    }
}