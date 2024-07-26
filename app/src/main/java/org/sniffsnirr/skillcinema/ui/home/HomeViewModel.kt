package org.sniffsnirr.skillcinema.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import org.sniffsnirr.skillcinema.entities.Movie
import org.sniffsnirr.skillcinema.restrepository.KinopoiskRepository

class HomeViewModel : ViewModel() {
    private val kinopoiskRepository = KinopoiskRepository()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _premiers = MutableStateFlow<List<Movie>>(emptyList())
    val premiers = _premiers.asStateFlow()

    init {
        loadPremiers()
    }

    private fun loadPremiers() {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                _isLoading.value = true
                kinopoiskRepository.getPremieres()
            }.fold(
                onSuccess = { _premiers.value = it },
                onFailure = { Log.d("MovieListViewModel", it.message ?: "") }
            )
            _isLoading.value = false
        }
    }

}




