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
import org.sniffsnirr.skillcinema.usecases.GetViewedMoviesUsecase
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(val getViewedMoviesUsecase: GetViewedMoviesUsecase): ViewModel() {
    private val _viewedMovies = MutableStateFlow<List<MovieRVModel>?>(null)
    val viewedMovies = _viewedMovies.asStateFlow()

    init {
        loadMoviesCollectionsForHomePage()
    }

    private fun loadMoviesCollectionsForHomePage() {
        viewModelScope.launch(Dispatchers.IO) {// Запуск загрузки всего контента
            kotlin.runCatching {

                getViewedMoviesUsecase.getViewedMoviesFromAPI(ProfileFragment.ID_VIEWED_COLLECTION)
            }.fold(
                onSuccess = { _viewedMovies.value = it },
                onFailure = { Log.d("MovieListViewModel", it.message ?: "") }
            )

        }
    }


}