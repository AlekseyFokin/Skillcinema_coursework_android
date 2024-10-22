package org.sniffsnirr.skillcinema.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sniffsnirr.skillcinema.entities.onlyonemovie.OnlyOneMovie
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import org.sniffsnirr.skillcinema.usecases.DeleteAllMoviesFromCollectionUsecase
import org.sniffsnirr.skillcinema.usecases.GetCountDbCollectionUsecase
import org.sniffsnirr.skillcinema.usecases.GetViewedMoviesUsecase
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val getViewedMoviesUsecase: GetViewedMoviesUsecase,
    val getCountDbCollectionUsecase: GetCountDbCollectionUsecase,
    val deleteAllMoviesFromCollectionUsecase: DeleteAllMoviesFromCollectionUsecase
) : ViewModel() {
    private val _viewedMovies = MutableStateFlow<List<MovieRVModel>?>(emptyList())
    val viewedMovies = _viewedMovies.asStateFlow()

    private val _countViewedMovies = MutableStateFlow<Int>(0)
    val countViewedMovies = _countViewedMovies.asStateFlow()

    fun loadViewedMovies() {
        viewModelScope.launch(Dispatchers.IO) {// Запуск загрузки коллекции просмотренных фильмов
            kotlin.runCatching {
                getViewedMoviesUsecase.getViewedMovies(ProfileFragment.ID_VIEWED_COLLECTION)
            }.fold(
                onSuccess = { _viewedMovies.value = it },
                onFailure = { Log.d("ViewedList", it.message ?: "") }
            )
        }
        viewModelScope.launch(Dispatchers.IO) {// Запуск загрузки размера коллекции просмотренных фильмов
            kotlin.runCatching {
                getCountDbCollectionUsecase.getCountCollection(ProfileFragment.ID_VIEWED_COLLECTION)
            }.fold(
                onSuccess = { _countViewedMovies.value = it },
                onFailure = { Log.d("ViewedList", it.message ?: "") }
            )
        }
    }

    fun clearViewedCollection() {
        viewModelScope.launch(Dispatchers.IO) {
            deleteAllMoviesFromCollectionUsecase.deleteAllMovieFromCollection(ProfileFragment.ID_VIEWED_COLLECTION)
            loadViewedMovies()
        }

    }


}