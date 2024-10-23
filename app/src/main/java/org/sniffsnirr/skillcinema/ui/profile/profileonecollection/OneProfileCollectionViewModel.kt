package org.sniffsnirr.skillcinema.ui.profile.profileonecollection

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
class OneProfileCollectionViewModel @Inject constructor(val getMoviePremiers: GetMoviePremiers) :
    ViewModel() {
    private val _moviesInCollection = MutableStateFlow<List<MovieRVModel>?>(null)
    val moviesInCollection = _moviesInCollection.asStateFlow()

    fun loadMoviesInCollection() {
        viewModelScope.launch(Dispatchers.IO) {// Запуск загрузки
            kotlin.runCatching {
                
            }.fold(
                onSuccess = { _premierMovies.value = it },
                onFailure = { Log.d("перезагрузка премьер", it.message ?: "") }
            )
        }
    }
}