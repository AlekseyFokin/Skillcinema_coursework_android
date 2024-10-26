package org.sniffsnirr.skillcinema.ui.onemovie.dialogmovietocollection

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sniffsnirr.skillcinema.room.dbo.CollectionCountMovies
import org.sniffsnirr.skillcinema.usecases.GetCollectionAndCountMoviesWithMarkUsecase
import javax.inject.Inject

@HiltViewModel
class DialogMovieToCollectionViewModel @Inject constructor(val getCollectionAndCountMoviesWithMarkUsecase: GetCollectionAndCountMoviesWithMarkUsecase) :
    ViewModel() {
    private val _collectionsForRV = MutableStateFlow<List<Pair<CollectionCountMovies,Boolean>>?>(emptyList())
    val collectionsForRV = _collectionsForRV.asStateFlow()


    fun loadCollections(movieId:Long) {
        viewModelScope.launch(Dispatchers.IO) {// Запуск загрузки коллекций
            kotlin.runCatching {
                getCollectionAndCountMoviesWithMarkUsecase.getCollectionAndCountMoviesWithMark(movieId)
            }.fold(
                onSuccess = { _collectionsForRV.value = it },
                onFailure = { Log.d("ViewedList", it.message ?: "") }
            )
        }
    }
}