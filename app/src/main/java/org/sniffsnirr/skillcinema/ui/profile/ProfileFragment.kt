package org.sniffsnirr.skillcinema.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.sniffsnirr.skillcinema.databinding.FragmentProfileBinding
import org.sniffsnirr.skillcinema.ui.home.adapter.MainAdapter
import org.sniffsnirr.skillcinema.ui.onemovie.adapter.GalleryAdapter
import org.sniffsnirr.skillcinema.ui.profile.adapter.ViewedAdapter

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewedAdapter = ViewedAdapter()
    //{ collectionModel -> onCollectionClick(collectionModel) },
    //{ idMovie -> onMovieClick(idMovie) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
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


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val LIMIT_FOR_RV = 8
        const val ID_FAVORITE_COLLECTION = 1L
        const val ID_SCHEDULED_COLLECTION = 2L
        const val ID_VIEWED_COLLECTION = 3L
        const val ID_INTERESTED_COLLECTION = 4L
    }
}