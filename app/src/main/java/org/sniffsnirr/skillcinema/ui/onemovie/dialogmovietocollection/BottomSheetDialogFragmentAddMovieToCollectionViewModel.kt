package org.sniffsnirr.skillcinema.ui.onemovie.dialogmovietocollection

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sniffsnirr.skillcinema.App.Companion.POSTERS_DIR
import org.sniffsnirr.skillcinema.room.dbo.CollectionCountMovies
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import org.sniffsnirr.skillcinema.usecases.CreateCollectionUsecase
import org.sniffsnirr.skillcinema.usecases.DeleteMovieFromCollectionUsecase
import org.sniffsnirr.skillcinema.usecases.GetCollectionAndCountMoviesWithMarkUsecase
import org.sniffsnirr.skillcinema.usecases.GetOneMovieFromDBByCollectionUsecase
import org.sniffsnirr.skillcinema.usecases.InsertNewMovieToCollectionUsecase
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class BottomSheetDialogFragmentAddMovieToCollectionViewModel @Inject constructor(
    val getCollectionAndCountMoviesWithMarkUsecase: GetCollectionAndCountMoviesWithMarkUsecase,
    val createCollectionUsecase: CreateCollectionUsecase,
    val insertNewMovieToCollectionUsecase: InsertNewMovieToCollectionUsecase,
    val deleteMovieFromCollectionUsecase: DeleteMovieFromCollectionUsecase,
    val getOneMovieFromDBByCollectionUsecase: GetOneMovieFromDBByCollectionUsecase
) :
    ViewModel() {
var    movieId: Long=0
    private val _collectionsForRV =
        MutableStateFlow<List<Pair<CollectionCountMovies, Boolean>>?>(emptyList())
    val collectionsForRV = _collectionsForRV.asStateFlow()


    fun loadCollections(movieId: Long) {
        this.movieId=movieId//сохраню для перезагрузки коллекций
        viewModelScope.launch(Dispatchers.IO) {// Запуск загрузки коллекций
            kotlin.runCatching {
                getCollectionAndCountMoviesWithMarkUsecase.getCollectionAndCountMoviesWithMark(
                    movieId
                )
            }.fold(
                onSuccess = { _collectionsForRV.value = it },
                onFailure = { Log.d("ViewedList", it.message ?: "") }
            )
        }
    }

    fun createCollection(nameCollection: String) {
        viewModelScope.launch(Dispatchers.IO) {
            createCollectionUsecase.insertCollection(nameCollection)
            loadCollections(movieId)
        }
    }

    fun addNewMovieToCollection(movieRVModel: MovieRVModel,idCollection:Long,dir:File,bitmap:Bitmap){
        viewModelScope.launch(Dispatchers.IO) {
            insertNewMovieToCollectionUsecase.addNewMovie(movieRVModel,idCollection,dir,bitmap)
        //перезагрузить коллекции
        loadCollections(movieId)
        }
    }

    fun deleteMovieFromCollection(movieRVModel: MovieRVModel,idCollection:Long,dir:File){
        viewModelScope.launch(Dispatchers.IO) {
            // удаляю из БД
            deleteMovieFromCollectionUsecase.deleteMovieFromCollection(movieRVModel,idCollection,dir)
            //перезагрузить коллекции
            loadCollections(movieId)
        }
    }


}