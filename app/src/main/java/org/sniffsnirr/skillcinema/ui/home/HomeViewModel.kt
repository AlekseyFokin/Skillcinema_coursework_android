package org.sniffsnirr.skillcinema.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sniffsnirr.skillcinema.ui.home.model.MainModel
import org.sniffsnirr.skillcinema.usecases.HomePageUsecase
import javax.inject.Inject

@HiltViewModel
//class HomeViewModel @Inject constructor(val kinopoiskRepository : KinopoiskRepository): ViewModel() {
class HomeViewModel @Inject constructor(val homePageUsecase: HomePageUsecase):ViewModel() {
    //private val kinopoiskRepository = KinopoiskRepository()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _moviesCollectionsForHomePage = MutableStateFlow<List<MainModel>>(emptyList())
    val moviesCollectionsForHomePage = _moviesCollectionsForHomePage.asStateFlow()

    init {
        loadMoviesCollectionsForHomePage()
    }

    private fun loadMoviesCollectionsForHomePage() {
        viewModelScope.launch(Dispatchers.IO) {// Запуск загрузки всего контента
            kotlin.runCatching {
                _isLoading.value = true
                homePageUsecase.getHomePageCollections()
            }.fold(
                onSuccess = { _moviesCollectionsForHomePage.value = it },
                onFailure = { Log.d("MovieListViewModel", it.message ?: "") }
            )
            _isLoading.value = false
        }
    }

}




