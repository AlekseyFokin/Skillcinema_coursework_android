package org.sniffsnirr.skillcinema.ui.onemovie

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sniffsnirr.skillcinema.entities.images.Image
import org.sniffsnirr.skillcinema.entities.onlyonemovie.OnlyOneMovie
import org.sniffsnirr.skillcinema.entities.staff.Staff
import org.sniffsnirr.skillcinema.room.dbo.MovieDBO
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import org.sniffsnirr.skillcinema.ui.profile.ProfileFragment
import org.sniffsnirr.skillcinema.usecases.GetActorsAndMoviemen
import org.sniffsnirr.skillcinema.usecases.GetCountMovieInCollection
import org.sniffsnirr.skillcinema.usecases.GetImages
import org.sniffsnirr.skillcinema.usecases.GetMovieInfo
import org.sniffsnirr.skillcinema.usecases.GetRelatedMoviesInfo
import org.sniffsnirr.skillcinema.usecases.GetSerialInfo
import org.sniffsnirr.skillcinema.usecases.InsertNewMovieUsecase
import javax.inject.Inject

@HiltViewModel
class OneMovieViewModel @Inject constructor(
    val getMovieInfo: GetMovieInfo,
    val getActorsAndMoviemen: GetActorsAndMoviemen,
    val getImages: GetImages,
    val getRelatedMoviesInfo: GetRelatedMoviesInfo,
    val getSerialInfo: GetSerialInfo,
    val insertNewMovieUsecase: InsertNewMovieUsecase,
    val getCountMovieInCollection: GetCountMovieInCollection
) : ViewModel() {
    val idMovie: Int = 0

    private val _movieInfo = MutableStateFlow<OnlyOneMovie?>(null)
    val movieInfo = _movieInfo.asStateFlow()

    private val _actorsInfo = MutableStateFlow<List<Staff>>(emptyList())
    val actorsInfo = _actorsInfo.asStateFlow()

    private val _movieMenInfo = MutableStateFlow<List<Staff>>(emptyList())
    val movieMenInfo = _movieMenInfo.asStateFlow()

    private val _gallery = MutableStateFlow<List<Image>>(emptyList())
    val gallery = _gallery.asStateFlow()

    private val _relatedMovies = MutableStateFlow<List<MovieRVModel>>(emptyList())
    val relatedMovies = _relatedMovies.asStateFlow()

    private val _numberseries = MutableStateFlow(0)
    val numberseries = _numberseries.asStateFlow()

    private val _isFavoriteMovie = MutableStateFlow<Boolean>(false)
    val isFavoriteMovie = _isFavoriteMovie.asStateFlow()

    fun setIdMovie(idMovie: Int) {
        viewModelScope.launch(Dispatchers.IO) {// получение информации о фильме, как только установлен id фильма из bundle
            kotlin.runCatching {
                getMovieInfo.getInfo(idMovie)
            }.fold(
                onSuccess = { _movieInfo.value = it },
                onFailure = { Log.d("MovieInfo", it.message ?: "") }
            )
        }
        getActorsAndMoviemen(idMovie)
    }

    private fun getActorsAndMoviemen(idMovie: Int) {
        viewModelScope.launch(Dispatchers.IO) {// получение и актеров и кинематографистов  по фильму
            kotlin.runCatching {
                getActorsAndMoviemen.getActorsAndMoviemen(idMovie)
            }.fold(
                onSuccess = {
                    _actorsInfo.value = it.first
                    _movieMenInfo.value = it.second
                },
                onFailure = { Log.d("ActorsAndMoviemen", it.message ?: "") }
            )
        }
        getImages(idMovie)
    }

    private fun getImages(idMovie: Int) {// получение изображений
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                getImages.getImages(idMovie)
            }.fold(
                onSuccess = { _gallery.value = it },
                onFailure = { Log.d("Gallery", it.message ?: "") }
            )
        }
        getRelatedMovies(idMovie)
    }

    private fun getRelatedMovies(idMovie: Int) {// получение похожих фильмов
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                getRelatedMoviesInfo.getRelatedMoviesRVModel(idMovie)
            }.fold(
                onSuccess = { _relatedMovies.value = it },
                onFailure = { Log.d("relatedMovies", it.message ?: "") }
            )
        }
    }

    fun getNumberEpisodsOfFirstSeason(idMovie: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                getSerialInfo.getNumberOfEpisodesOfFirstSeason(idMovie)
            }.fold(
                onSuccess = { _numberseries.value = it },
                onFailure = { Log.d("numberseries", it.message ?: "") }
            )
        }
    }

    fun addOrDeleteMovieToFavorite(kinopoiskId: Int): Boolean {
        var isSaved=false
        if (kinopoiskId != 0) {
            viewModelScope.launch(Dispatchers.IO) {
                if (!getCountMovieInCollection.isAlreadyExist(
                        kinopoiskId.toLong(),
                        ProfileFragment.ID_FAVORITE_COLLECTION,
                    )
                ) {
                    insertNewMovieUsecase.addNewMovie(
                        kinopoiskId.toLong(),
                        ProfileFragment.ID_FAVORITE_COLLECTION,
                    )
                    isSaved=true
                }
              }
            return isSaved
           } else {
         return false
        }
    }
}