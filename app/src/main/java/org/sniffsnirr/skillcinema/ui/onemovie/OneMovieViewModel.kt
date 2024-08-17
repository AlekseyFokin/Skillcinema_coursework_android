package org.sniffsnirr.skillcinema.ui.onemovie

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sniffsnirr.skillcinema.entities.onlyonemovie.OnlyOneMovie
import org.sniffsnirr.skillcinema.entities.staff.Staff
import org.sniffsnirr.skillcinema.ui.home.model.MainModel
import org.sniffsnirr.skillcinema.usecases.GetActorsAndMoviemen
import org.sniffsnirr.skillcinema.usecases.GetMovieInfo
import javax.inject.Inject

@HiltViewModel
class OneMovieViewModel @Inject constructor(val getMovieInfo: GetMovieInfo,val getActorsAndMoviemen: GetActorsAndMoviemen)  : ViewModel() {
    val idMovie:Int=0

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _movieInfo = MutableStateFlow<OnlyOneMovie?>(null)
    val movieInfo = _movieInfo.asStateFlow()

    private val _actorsInfo = MutableStateFlow<List<Staff>>(emptyList())
    val actorsInfo = _actorsInfo.asStateFlow()

    private val _movieMenInfo = MutableStateFlow<List<Staff>>(emptyList())
    val movieMenInfo = _movieMenInfo.asStateFlow()

    private val _gallery = MutableStateFlow<List<MainModel>>(emptyList())
    val gallery = _gallery.asStateFlow()

    private val _relatedMovies = MutableStateFlow<List<MainModel>>(emptyList())
    val relatedMovies = _relatedMovies.asStateFlow()

    fun setIdMovie(idMovie:Int){
        viewModelScope.launch(Dispatchers.IO) {// Запуск загрузки всего контента
            kotlin.runCatching {
                _isLoading.value = true
                getMovieInfo.getInfo(idMovie)
            }.fold(
                onSuccess = { _movieInfo.value = it },
                onFailure = { Log.d("MovieInfo", it.message ?: "") }
            )
            _isLoading.value = false
        }

        getActorsAndMoviemen(idMovie)
    }

    fun getActorsAndMoviemen(idMovie:Int){
        viewModelScope.launch(Dispatchers.IO) {// Запуск загрузки всего контента
            kotlin.runCatching {
                _isLoading.value = true
                getActorsAndMoviemen.getActorsAndMoviemen(idMovie)
            }.fold(
                onSuccess = { _actorsInfo.value = it.first
                              _movieMenInfo.value=it.second},
                onFailure = { Log.d("ActorsAndMoviemen", it.message ?: "") }
            )
            _isLoading.value = false
        }
    }
}