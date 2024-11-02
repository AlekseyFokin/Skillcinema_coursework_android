package org.sniffsnirr.skillcinema.ui.profile

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.widget.AppCompatButton
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.sniffsnirr.skillcinema.R
import org.sniffsnirr.skillcinema.databinding.FragmentProfileBinding
import org.sniffsnirr.skillcinema.room.dbo.CollectionCountMovies
import org.sniffsnirr.skillcinema.room.dbo.CollectionDBO
import org.sniffsnirr.skillcinema.ui.collections.paging.presets.PagingCollectionFragment
import org.sniffsnirr.skillcinema.ui.home.HomeFragment.Companion.ID_MOVIE
import org.sniffsnirr.skillcinema.ui.profile.adapter.CollectionAdapter
import org.sniffsnirr.skillcinema.ui.profile.adapter.InterestedAdapter
import org.sniffsnirr.skillcinema.ui.profile.adapter.ViewedAdapter

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewedAdapter = ViewedAdapter({ onClearAllViewedClick() },
        { idMovie, position -> onMovieClick(idMovie, position) })

    private val interestedAdapter = InterestedAdapter({ onClearAllInterestedClick() },
        { idMovie, position -> onMovieClick(idMovie, position) })

    private val collectionAdapter =
        CollectionAdapter({ collection -> onDeleteCollectionClick(collection) },
            { collection -> onOpenCollectionClick(collection) })

    private var possiblyEditablePosition = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        viewModel.loadViewedMovies()
        viewModel.loadInterestedMovies()
        viewModel.loadCollections()
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
        viewLifecycleOwner.lifecycleScope.launch {// загрузка количества просмотренных
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.countViewedMovies.collect {
                    binding.allViewedMoviesButton.text = it.toString()
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
                    binding.allInterestedButton.text = it.toString()
                }
            }
        }

        val collectionGridLayout =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        binding.collectionsRv.layoutManager = collectionGridLayout
        binding.collectionsRv.layoutManager = collectionGridLayout
        binding.collectionsRv.setHasFixedSize(true)
        binding.collectionsRv.adapter = collectionAdapter
        viewLifecycleOwner.lifecycleScope.launch {// загрузка RV коллекций
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.collectionsForRV.collect {
                    collectionAdapter.setData(it)
                }
            }
        }
        binding.allInterestedButton.setOnClickListener { onOpenCollectionClick(viewModel.interestedCollection.value) }
        binding.allViewedMoviesButton.setOnClickListener { onOpenCollectionClick(viewModel.viewedCollection.value) }

        binding.addNewCollectionBtn.setOnClickListener { showCustomDialog() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onMovieClick(idMovie: Int?, position: Int) {
        val bundle = Bundle()
        if (idMovie != null) {
            bundle.putInt(ID_MOVIE, idMovie)
            findNavController().navigate(
                R.id.action_navigation_profile_to_oneMovieFragment,
                bundle
            )
        }
        possiblyEditablePosition = position
    }

    private fun onDeleteCollectionClick(collection: CollectionCountMovies) {
        viewModel.deleteCollection(collection)
    }

    private fun onOpenCollectionClick(collection: CollectionDBO?) {
        if (collection != null) {
            val bundle = Bundle()
            bundle.putInt(ID_COLLECTION, collection.id.toInt())
            bundle.putCharSequence(NAME_COLLECTION, collection.name)
            findNavController().navigate(
                R.id.action_navigation_profile_to_oneProfileCollectionFragment,
                bundle
            )
        }
    }

    override fun onResume() {// если при восстановлении фрагмента получен сигнал об изменении данных - обновить состояние и передать выше
        super.onResume()
        setFragmentResultListener(PagingCollectionFragment.RV_ITEM_HAS_BEEN_CHANGED_REQUEST_KEY) { REQUEST_KEY, bundle ->
            if (bundle.getBoolean(PagingCollectionFragment.RV_ITEM_HAS_BEEN_CHANGED_BUNDLE_KEY) != null) {
                if (bundle.getBoolean(PagingCollectionFragment.RV_ITEM_HAS_BEEN_CHANGED_BUNDLE_KEY)) {
                    Log.d("Update!@#$", "Обновляю RV на ProfileFragment из OneMovieFragment")
                    viewedAdapter.deleteFromRV(possiblyEditablePosition)
                    //проброс изменения viewed
                    setFragmentResult(//сигнал на home фрагмент - нужно обновить rv
                        RV_ITEM_HAS_BEEN_CHANGED_IN_PRIFILE_FRAGMENT_REQUEST_KEY,
                        bundleOf(RV_ITEM_HAS_BEEN_CHANGED_IN_PRIFILE_FRAGMENT_BUNDLE_KEY to true)
                    )
                }
            }
        }
    }

    private fun onClearAllViewedClick() {
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

    private fun onClearAllInterestedClick() {
        viewModel.clearInterstedCollection()
    }

    private fun showCustomDialog() {//диалог создания новой коллекции
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_create_collection)
        val editText = dialog.findViewById<EditText>(R.id.new_collection_name)
        val positiveButton = dialog.findViewById<AppCompatButton>(R.id.go_btn)
        val negativeButton = dialog.findViewById<ImageButton>(R.id.close_btn)

        positiveButton.setOnClickListener {
            if (!editText.text.isNullOrEmpty()) {
                viewModel.createCollection(editText.text.toString())
            }
            dialog.dismiss()
        }
        negativeButton.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    companion object {
        const val LIMIT_FOR_RV = 8
        const val ID_FAVORITE_COLLECTION = 1L
        const val ID_WANT_TO_SEE_COLLECTION = 2L
        const val ID_VIEWED_COLLECTION = 3L
        const val ID_INTERESTED_COLLECTION = 4L

        const val RV_ITEM_HAS_BEEN_CHANGED_IN_PRIFILE_FRAGMENT_REQUEST_KEY = "PRIFILE_FRAGMENT"
        const val RV_ITEM_HAS_BEEN_CHANGED_IN_PRIFILE_FRAGMENT_BUNDLE_KEY = "PRIFILE_FRAGMENT"

        const val ID_COLLECTION = "ID_COLLECTION"
        const val NAME_COLLECTION = "NAME_COLLECTION"

        const val LIMIT_FOR_INTERESTED_COLLECTION = 20
    }
}