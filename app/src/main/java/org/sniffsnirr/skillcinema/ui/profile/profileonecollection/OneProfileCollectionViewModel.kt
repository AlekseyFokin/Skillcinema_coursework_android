package org.sniffsnirr.skillcinema.ui.profile.profileonecollection

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
import org.sniffsnirr.skillcinema.usecases.GetMoviesFromDBByCollectionUsecase
import javax.inject.Inject

@HiltViewModel
class OneProfileCollectionViewModel @Inject constructor(private val getMoviesFromDBByCollectionUsecase: GetMoviesFromDBByCollectionUsecase) :
    ViewModel() {
    private val _moviesInCollection = MutableStateFlow<List<MovieRVModel>?>(null)
    val moviesInCollection = _moviesInCollection.asStateFlow()

    private val _error = Channel<Boolean>() // для передачи ошибки соединения с сервисом поиска
    val error = _error.receiveAsFlow()

    fun loadMoviesInCollection(idCollection: Long) {
        viewModelScope.launch(Dispatchers.IO) {// Запуск загрузки Получение всех фильмов из коллекции
            kotlin.runCatching {
                getMoviesFromDBByCollectionUsecase.getMoviesByCollection(idCollection)
            }.fold(
                onSuccess = { _moviesInCollection.value = it },
                onFailure = {
                    Log.d("Error", "Получение всех фильмов из коллекции: ${it.message}")
                    _error.send(true)  // показывать диалог с ошибкой - где onFailure
                }
            )
        }
    }
}