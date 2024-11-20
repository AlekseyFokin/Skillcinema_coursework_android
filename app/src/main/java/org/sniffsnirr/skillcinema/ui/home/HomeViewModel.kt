package org.sniffsnirr.skillcinema.ui.home

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
import org.sniffsnirr.skillcinema.ui.home.model.MainModel
import org.sniffsnirr.skillcinema.usecases.HomePageUsecase
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val homePageUsecase: HomePageUsecase):ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _error = Channel<Boolean>() // для передачи ошибки соединения с сервисом поиска
    val error = _error.receiveAsFlow()

    private val _moviesCollectionsForHomePage = MutableStateFlow<List<MainModel>>(emptyList())
    val moviesCollectionsForHomePage = _moviesCollectionsForHomePage.asStateFlow()

    init {
        loadMoviesCollectionsForHomePage()
    }

    fun loadMoviesCollectionsForHomePage() {
        viewModelScope.launch(Dispatchers.IO) {// Запуск загрузки всего контента
            kotlin.runCatching {
                _isLoading.value = true
                homePageUsecase.getHomePageCollections()
            }.fold(
                onSuccess = { _moviesCollectionsForHomePage.value = it },
                onFailure = { Log.d("Error", "Загрузка Home : ${it.message}")
                    _isLoading.value = false
                    _error.send(true)  // показывать диалог с ошибкой - где onFailure
                }
            )
            _isLoading.value = false
        }
    }

}




