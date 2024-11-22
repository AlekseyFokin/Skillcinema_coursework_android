package org.sniffsnirr.skillcinema.ui.collections.premieres

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
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import org.sniffsnirr.skillcinema.usecases.GetMoviePremiersUsecase
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(private val getMoviePremiersUsecase: GetMoviePremiersUsecase) :
    ViewModel() {
    private val _premierMovies = MutableStateFlow<List<MovieRVModel>?>(null)
    val premierMovies = _premierMovies.asStateFlow()

    private val _error = Channel<Boolean>() // для передачи ошибки соединения с сервисом поиска
    val error = _error.receiveAsFlow()

    fun loadPremiers() {
        viewModelScope.launch(Dispatchers.IO) {// Запуск загрузки
            kotlin.runCatching {
                getMoviePremiersUsecase.getPremiersForNextTwoWeek()
            }.fold(
                onSuccess = { _premierMovies.value = it },
                onFailure = { Log.d("Error", "Загрузка премьер: ${it.message}")
                              _error.send(true)  // показывать диалог с ошибкой
                }
            )
        }
    }
}