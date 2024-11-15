package org.sniffsnirr.skillcinema.ui.onemovie.dialogmovietocollection

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.sniffsnirr.skillcinema.room.dbo.CollectionCountMovies
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import org.sniffsnirr.skillcinema.usecases.CreateCollectionUsecase
import org.sniffsnirr.skillcinema.usecases.DeleteMovieFromCollectionUsecase
import org.sniffsnirr.skillcinema.usecases.GetCollectionAndCountMoviesWithMarkUsecase
import org.sniffsnirr.skillcinema.usecases.InsertNewMovieToCollectionUsecase
import java.io.File
import javax.inject.Inject

@HiltViewModel
class BottomSheetDialogFragmentAddMovieToCollectionViewModel @Inject constructor(
    private val getCollectionAndCountMoviesWithMarkUsecase: GetCollectionAndCountMoviesWithMarkUsecase,
    private val createCollectionUsecase: CreateCollectionUsecase,
    private val insertNewMovieToCollectionUsecase: InsertNewMovieToCollectionUsecase,
    val deleteMovieFromCollectionUsecase: DeleteMovieFromCollectionUsecase,

    ) :
    ViewModel() {
    private var movieId: Long = 0
    private val _collectionsForRV =
        MutableStateFlow<List<Pair<CollectionCountMovies, Boolean>>?>(emptyList())
    val collectionsForRV = _collectionsForRV.asStateFlow()

    private val _error = Channel<Boolean>() // для передачи ошибки соединения с сервисом поиска
    val error = _error.receiveAsFlow()


    fun loadCollections(movieId: Long) {
        this.movieId = movieId//сохраню для перезагрузки коллекций
        viewModelScope.launch(Dispatchers.IO) {// Запуск загрузки коллекций
            kotlin.runCatching {
                getCollectionAndCountMoviesWithMarkUsecase.getCollectionAndCountMoviesWithMark(
                    movieId
                )
            }.fold(
                onSuccess = { _collectionsForRV.value = it },
                onFailure = {
                    Log.d("Error", "Загрузка коллекций: ${it.message}")
                    _error.send(true)  // показывать диалог с ошибкой - где onFailure
                }
            )
        }
    }

    fun createCollection(nameCollection: String) {
        viewModelScope.launch(Dispatchers.IO) {
            createCollectionUsecase.insertCollection(nameCollection)
            loadCollections(movieId)
        }
    }

    fun addNewMovieToCollection(
        movieRVModel: MovieRVModel,
        idCollection: Long,
        dir: File,
        bitmap: Bitmap
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            insertNewMovieToCollectionUsecase.addNewMovie(movieRVModel, idCollection, dir, bitmap)
            //перезагрузить коллекции
            loadCollections(movieId)
        }
    }

    fun deleteMovieFromCollection(movieRVModel: MovieRVModel, idCollection: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            // удаляю из БД
            deleteMovieFromCollectionUsecase.deleteMovieFromCollection(movieRVModel, idCollection)
            //перезагрузить коллекции
            loadCollections(movieId)
        }
    }


}