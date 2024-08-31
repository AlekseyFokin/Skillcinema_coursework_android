package org.sniffsnirr.skillcinema.ui.movieman.filmography

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sniffsnirr.skillcinema.entities.movieman.Film
import org.sniffsnirr.skillcinema.usecases.GetMoviemanFilmography
import javax.inject.Inject

@HiltViewModel
class FilmographyViewModel @Inject constructor(val getMoviemanFilmography: GetMoviemanFilmography) : ViewModel() {

    private val _moviemanName = MutableStateFlow<Pair<String,String>>(Pair("",""))
    var moviemanName = _moviemanName.asStateFlow()

    private val _moviesByProfessionKey = MutableStateFlow<Map<String,List<Film>>>(emptyMap())
    var moviesByProfessionKey = _moviesByProfessionKey.asStateFlow()

    fun getMoviesByProfessionKey(idStaff:Int){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                getMoviemanFilmography.getMovieByMovieman(idStaff)
            }.fold(
                onSuccess = {
                    _moviemanName.value = it.first
                    _moviesByProfessionKey.value = it.second
                                    },
                onFailure = { Log.d("MoviesByFilmography", it.message ?: "") }
            )
        }

    }
}