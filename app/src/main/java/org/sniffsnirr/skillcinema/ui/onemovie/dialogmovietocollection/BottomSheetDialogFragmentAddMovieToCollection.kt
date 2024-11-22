package org.sniffsnirr.skillcinema.ui.onemovie.dialogmovietocollection

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.viewModels
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.sniffsnirr.skillcinema.App.Companion.POSTERS_DIR
import org.sniffsnirr.skillcinema.R
import org.sniffsnirr.skillcinema.databinding.FragmentAddMovieToCollectionBinding
import org.sniffsnirr.skillcinema.room.dbo.CollectionCountMovies
import org.sniffsnirr.skillcinema.ui.exception.BottomSheetErrorFragment
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import org.sniffsnirr.skillcinema.ui.onemovie.OneMovieFragment

//BottomSheetDialogFragment - всплывающее окно редактирования присутствия фильма в локальной базе данных коллекций
@AndroidEntryPoint
class BottomSheetDialogFragmentAddMovieToCollection : BottomSheetDialogFragment() {

    private val viewModel: BottomSheetDialogFragmentAddMovieToCollectionViewModel by viewModels()
    var _binding: FragmentAddMovieToCollectionBinding? = null
    val binding get() = _binding!!
    val adapter =
        DialogMovieToCollectionAdapter({ onPlusCollectionClick() },
            { listCollectionWithMark -> putCurrentListCollectionWithMark(listCollectionWithMark) })

    var movieRVModel: MovieRVModel? = null
    private lateinit var currentListCollectionWithMark: MutableList<Pair<CollectionCountMovies, Boolean>>
    lateinit var bitmap: Bitmap
    private var mastDismiss = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        movieRVModel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(OneMovieFragment.MOVIE, MovieRVModel::class.java)
        } else {
            arguments?.getParcelable(OneMovieFragment.MOVIE)
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

        CoroutineScope(Dispatchers.IO).launch {
            bitmap = Glide.with(binding.poster.context)
                .asBitmap()
                .load(movieRVModel?.imageUrl)
                .submit().get()
        }


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
        val dividerItemDecorator = DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
        dividerItemDecorator.setDrawable(resources.getDrawable(R.drawable.line_for_rv))
        binding.collectionRv.addItemDecoration(dividerItemDecorator)
        binding.collectionRv.setHasFixedSize(true)

        viewLifecycleOwner.lifecycleScope.launch {// загрузка RV коллекций
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.collectionsForRV.collect {
                    if (!it.isNullOrEmpty()) {
                        adapter.setContent(it)
                        currentListCollectionWithMark = it.toMutableList()
                        if (mastDismiss) {
                            dismiss()
                        }
                    }
                }
            }
        }

        binding.closeBtn.setOnClickListener {
            dismiss()
        }

        binding.headerForRv.setOnClickListener {//сохранить все указанное присутствие фильма в коллекциях
            saveMovieToAllCheckedCollection()
            mastDismiss = true
        }

        viewLifecycleOwner.lifecycleScope.launch {// ожидание ошибки
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.error.collect { _ ->
                    BottomSheetErrorFragment().show(parentFragmentManager, "errordialog")
                }
            }
        }
    }

    private fun saveMovieToAllCheckedCollection() {
        currentListCollectionWithMark.removeAt(currentListCollectionWithMark.lastIndex)//убираю последний элемент - так как он кнопка

        val dir = requireContext().getDir(
            POSTERS_DIR,
            Context.MODE_PRIVATE
        )// получаю директорию приложения
        currentListCollectionWithMark.map { pair ->// проход по всем коллекциям - если галка то добавляю коллекцию, если нет - то удаляю
            if (pair.second) {
                viewModel.addNewMovieToCollection(
                    movieRVModel!!,
                    pair.first.id,
                    dir,
                    bitmap
                )
            } else {//иначе удалить
                viewModel.deleteMovieFromCollection(
                    movieRVModel!!,
                    pair.first.id
                )
            }
        }
//если dismiss вызывать тут то отменяется сохранение в бд и в файле , поэтому доп переменная - mastDismiss
    }

    private fun onPlusCollectionClick() {
//сохраняю а то галочки потеряются
        saveMovieToAllCheckedCollection()
        showCustomDialog()
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

    private fun putCurrentListCollectionWithMark(listCollectionWithMark: List<Pair<CollectionCountMovies, Boolean>>) {
        currentListCollectionWithMark = listCollectionWithMark.toMutableList()
    }
}