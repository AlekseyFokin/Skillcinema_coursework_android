package org.sniffsnirr.skillcinema.ui.onemovie.dialogmovietocollection

import android.app.Dialog
import android.os.Build
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.sniffsnirr.skillcinema.R
import org.sniffsnirr.skillcinema.databinding.FragmentAddMovieToCollectionBinding
import org.sniffsnirr.skillcinema.room.dbo.CollectionCountMovies
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import org.sniffsnirr.skillcinema.ui.onemovie.OneMovieFragment

@AndroidEntryPoint
class BottomSheetDialogFragmentAddMovieToCollection : BottomSheetDialogFragment() {

    private val viewModel: BottomSheetDialogFragmentAddMovieToCollectionViewModel by viewModels()
    var _binding: FragmentAddMovieToCollectionBinding? = null
    val binding get() = _binding!!
    val collectionListForAddMovie = mutableListOf<Int>()
    val adapter =
        DialogMovieToCollectionAdapter({ onPlusCollectionClick() },
            { ListCollectionWithMark -> putCurrentListCollectionWithMark(ListCollectionWithMark) });

    var movieRVModel: MovieRVModel? = null
    lateinit var currentListCollectionWithMark: List<Pair<CollectionCountMovies, Boolean>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            movieRVModel =
                arguments?.getParcelable(OneMovieFragment.MOVIE, MovieRVModel::class.java)
        } else {
            movieRVModel = arguments?.getParcelable<MovieRVModel>(OneMovieFragment.MOVIE)
        }
        viewModel.loadCollections(movieRVModel?.kinopoiskId?.toLong() ?: 0)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddMovieToCollectionBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide
            .with(binding.poster.context)
            .load(movieRVModel?.imageUrl)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(binding.poster)

        binding.genre.text = movieRVModel?.movieGenre ?: ""
        binding.movieName.text = movieRVModel?.movieName ?: ""
        binding.raiting.text = movieRVModel?.rate

        binding.collectionRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.collectionRv.adapter = adapter
        //установка декоратора
        val dividerItemDecorator= DividerItemDecoration(requireContext(),RecyclerView.VERTICAL)
        dividerItemDecorator.setDrawable(resources.getDrawable(R.drawable.line_for_rv))
        binding.collectionRv.addItemDecoration(dividerItemDecorator)

        binding.collectionRv.setHasFixedSize(true)

        viewLifecycleOwner.lifecycleScope.launch {// загрузка RV коллекций
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.collectionsForRV.collect {
                    if (!it.isNullOrEmpty()) {
                        adapter.setContent(it)
                        currentListCollectionWithMark = it
                    }
                }
            }
        }

        binding.closeBtn.setOnClickListener {
            dismiss()
        }

        binding.headerForRv.setOnClickListener {//сохранить все указанное присутствие фильма в коллекциях
            currentListCollectionWithMark.map { pair ->
                if (pair.second) {
                    viewModel.addNewMovieToCollection(
                        (movieRVModel?.kinopoiskId ?: 0).toLong(),
                        pair.first.id
                    )
                } else {//иначе удалить
                    viewModel.deleteMovieFromCollection((movieRVModel?.kinopoiskId ?: 0).toLong(),
                    pair.first.id)
                }
            }
            dismiss()
        }
    }

    fun onPlusCollectionClick() {
        showCustomDialog()
    }

    fun showCustomDialog() {//диалог создания новой коллекции
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_create_collection)
        val editText = dialog.findViewById<EditText>(R.id.new_collection_name)
        val positiveButton = dialog.findViewById<AppCompatButton>(R.id.go_btn)
        val negativeButton = dialog.findViewById<ImageButton>(R.id.close_btn)

        positiveButton.setOnClickListener {
            if (!editText.text.isNullOrEmpty()) {
                viewModel.createCollection(editText.text.toString())
                //adapter.notifyItemInserted((viewModel.collectionsForRV.value?.size ?: 1) - 1)
                //  adapter.setContent(currentListCollectionWithMark)
            }
            dialog.dismiss()
        }
        negativeButton.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    fun putCurrentListCollectionWithMark(listCollectionWithMark: List<Pair<CollectionCountMovies, Boolean>>) {
        currentListCollectionWithMark = listCollectionWithMark
    }
}