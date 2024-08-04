package org.sniffsnirr.skillcinema.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import org.sniffsnirr.skillcinema.entities.Movie
import org.sniffsnirr.skillcinema.restrepository.KinopoiskRepository
import org.sniffsnirr.skillcinema.ui.home.model.MainModel
import org.sniffsnirr.skillcinema.usecases.GetMoviePremiers
import javax.inject.Inject

@HiltViewModel
//class HomeViewModel @Inject constructor(val kinopoiskRepository : KinopoiskRepository): ViewModel() {
class HomeViewModel @Inject constructor(val getMoviePremiers: GetMoviePremiers):ViewModel() {
    //private val kinopoiskRepository = KinopoiskRepository()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _premiers = MutableStateFlow<List<MainModel>>(emptyList())
    val premiers = _premiers.asStateFlow()

    init {
        loadPremiers()
    }

    private fun loadPremiers() {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                _isLoading.value = true
                getMoviePremiers.getPremiersForNextTwoWeek()
            }.fold(
                onSuccess = { _premiers.value = it },
                onFailure = { Log.d("MovieListViewModel", it.message ?: "") }
            )
            _isLoading.value = false
        }
    }

}




