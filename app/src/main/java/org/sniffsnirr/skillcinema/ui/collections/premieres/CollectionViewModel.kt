package org.sniffsnirr.skillcinema.ui.collections.premieres

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import org.sniffsnirr.skillcinema.usecases.GetMoviePremiers
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(val getMoviePremiers: GetMoviePremiers) :
    ViewModel() {
    private val _premierMovies = MutableStateFlow<List<MovieRVModel>?>(null)
    val premierMovies = _premierMovies.asStateFlow()

    fun loadPremiers() {
        viewModelScope.launch(Dispatchers.IO) {// Запуск загрузки
            kotlin.runCatching {
                getMoviePremiers.getPremiersForNextTwoWeek()
            }.fold(
                onSuccess = { _premierMovies.value = it },
                onFailure = { Log.d("перезагрузка премьер", it.message ?: "") }
            )
        }
    }
}