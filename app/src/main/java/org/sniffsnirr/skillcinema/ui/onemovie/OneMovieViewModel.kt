package org.sniffsnirr.skillcinema.ui.onemovie

import android.graphics.Bitmap
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
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import org.sniffsnirr.skillcinema.ui.profile.ProfileFragment
import org.sniffsnirr.skillcinema.usecases.AddMovieToInterestedCollectionUsecase
import org.sniffsnirr.skillcinema.usecases.DeleteMovieFromCollectionUsecase
import org.sniffsnirr.skillcinema.usecases.GetActorsAndMoviemenUsecase
import org.sniffsnirr.skillcinema.usecases.GetCountMovieInCollectionUsecase
import org.sniffsnirr.skillcinema.usecases.GetImagesUsecase
import org.sniffsnirr.skillcinema.usecases.GetMovieInfoUsecase
import org.sniffsnirr.skillcinema.usecases.GetRelatedMoviesInfoUsecase
import org.sniffsnirr.skillcinema.usecases.GetSerialInfoUsecase
import org.sniffsnirr.skillcinema.usecases.InsertNewMovieToCollectionUsecase
import java.io.File
import javax.inject.Inject

@HiltViewModel
class OneMovieViewModel @Inject constructor(
    private val getMovieInfoUsecase: GetMovieInfoUsecase,
    private val getActorsAndMoviemenUsecase: GetActorsAndMoviemenUsecase,
    private val getImagesUsecase: GetImagesUsecase,
    private val getRelatedMoviesInfoUsecase: GetRelatedMoviesInfoUsecase,
    private val getSerialInfoUsecase: GetSerialInfoUsecase,
    private val insertNewMovieToCollectionUsecase: InsertNewMovieToCollectionUsecase,
    val getCountMovieInCollectionUsecase: GetCountMovieInCollectionUsecase,
    val deleteMovieFromCollectionUsecase: DeleteMovieFromCollectionUsecase,
    private val addMovieToInterestedCollectionUsecase: AddMovieToInterestedCollectionUsecase
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

    private val _isMovieInFavorite = MutableStateFlow(false)
    val isMovieInFavorite = _isMovieInFavorite.asStateFlow()

    private val _isMovieInWantToSee = MutableStateFlow(false)
    val isMovieInWantToSee = _isMovieInWantToSee.asStateFlow()

    private val _isMovieInViewed = MutableStateFlow(false)
    val isMovieInViewed = _isMovieInViewed.asStateFlow()

    fun setIdMovie(idMovie: Int) {
        viewModelScope.launch(Dispatchers.IO) {// получение информации о фильме, как только установлен id фильма из bundle
            kotlin.runCatching {
                getMovieInfoUsecase.getInfo(idMovie)
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
                getActorsAndMoviemenUsecase.getActorsAndMoviemen(idMovie)
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
                getImagesUsecase.getImages(idMovie)
            }.fold(
                onSuccess = { _gallery.value = it },
                onFailure = { Log.d("Gallery", it.message ?: "") }
            )
        }
        decideMovieInFavorite(idMovie)
    }
    private fun decideMovieInFavorite(idMovie: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _isMovieInFavorite.value = getCountMovieInCollectionUsecase.isAlreadyExist(
                idMovie.toLong(),
                ProfileFragment.ID_FAVORITE_COLLECTION
            )
        }
        decideMovieInWantToSee(idMovie)
    }

    fun decideMovieInWantToSee(idMovie: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _isMovieInWantToSee.value = getCountMovieInCollectionUsecase.isAlreadyExist(
                idMovie.toLong(),
                ProfileFragment.ID_WANT_TO_SEE_COLLECTION
            )
        }
        decideMovieInViewed(idMovie)
    }

    private fun decideMovieInViewed(idMovie: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _isMovieInViewed.value = getCountMovieInCollectionUsecase.isAlreadyExist(
                idMovie.toLong(),
                ProfileFragment.ID_VIEWED_COLLECTION            )
        }
      //  getRelatedMovies(idMovie)
    }

    fun getRelatedMovies(idMovie: Int) {// получение похожих фильмов
        Log.d("Update","Запрос на получение данных для RV на OneMovieFragment")
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                getRelatedMoviesInfoUsecase.getRelatedMoviesRVModel(idMovie)
            }.fold(
                onSuccess = { _relatedMovies.value = it },
                onFailure = { Log.d("relatedMovies", it.message ?: "") }
            )
        }
    }

    fun getNumberEpisodsOfFirstSeason(idMovie: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                getSerialInfoUsecase.getNumberOfEpisodesOfFirstSeason(idMovie)
            }.fold(
                onSuccess = { _numberseries.value = it },
                onFailure = { Log.d("numberseries", it.message ?: "") }
            )
        }
    }

    fun addOrDeleteMovieToFavorite(movieRVModel:MovieRVModel,dir:File,bitmap:Bitmap) { // нажатие на кнопку добавления в коллекцию любимых фильмов
        if (movieRVModel.kinopoiskId != 0) {
            viewModelScope.launch(Dispatchers.IO) {
                if (getCountMovieInCollectionUsecase.isAlreadyExist(
// если уже есть - исключить из коллекции Любимых фильмов
                        movieRVModel.kinopoiskId!!.toLong(),
                        ProfileFragment.ID_FAVORITE_COLLECTION,
                    )
                ) {
                     // удаляю из БД
                    deleteMovieFromCollectionUsecase.deleteMovieFromCollection(movieRVModel,ProfileFragment.ID_FAVORITE_COLLECTION)
                    _isMovieInFavorite.value = false
                } else { // иначе добавить
                    insertNewMovieToCollectionUsecase.addNewMovie(
                        movieRVModel,
                        ProfileFragment.ID_FAVORITE_COLLECTION,
                        dir,
                        bitmap
                    )
                    _isMovieInFavorite.value = true
                }
            }
        }
    }

    fun addOrDeleteMovieToWantToSee(movieRVModel:MovieRVModel,dir:File,bitmap:Bitmap) {// нажатие на кнопку добавления в коллекцию фильмов планируемых к просмотру
        if (movieRVModel.kinopoiskId != 0) {
            viewModelScope.launch(Dispatchers.IO) {
                if (getCountMovieInCollectionUsecase.isAlreadyExist(
// если уже есть - исключить из коллекции Любимых фильмов
                        movieRVModel.kinopoiskId!!.toLong(),
                        ProfileFragment.ID_WANT_TO_SEE_COLLECTION,
                    )
                ) {
                    deleteMovieFromCollectionUsecase.deleteMovieFromCollection(
                        movieRVModel,
                        ProfileFragment.ID_WANT_TO_SEE_COLLECTION)
                    _isMovieInWantToSee.value = false
                } else { // иначе добавить
                    insertNewMovieToCollectionUsecase.addNewMovie(
                        movieRVModel,
                        ProfileFragment.ID_WANT_TO_SEE_COLLECTION,
                        dir,
                        bitmap
                    )
                    _isMovieInWantToSee.value = true
                }
            }
        }
    }


    fun addOrDeleteMovieToViewed(movieRVModel:MovieRVModel,dir:File,bitmap:Bitmap) {// нажатие на кнопку добавления в коллекцию просмотренных фильмов
        if (movieRVModel.kinopoiskId != 0) {
            viewModelScope.launch(Dispatchers.IO) {
                if (getCountMovieInCollectionUsecase.isAlreadyExist(
// если уже есть - исключить из коллекции Любимых фильмов
                        movieRVModel.kinopoiskId!!.toLong(),
                        ProfileFragment.ID_VIEWED_COLLECTION,
                    )
                ) {
                    deleteMovieFromCollectionUsecase.deleteMovieFromCollection(
                        movieRVModel,
                        ProfileFragment.ID_VIEWED_COLLECTION)
                    _isMovieInViewed.value = false
                } else { // иначе добавить
                    insertNewMovieToCollectionUsecase.addNewMovie(
                        movieRVModel,
                        ProfileFragment.ID_VIEWED_COLLECTION,
                        dir,
                        bitmap
                    )
                    _isMovieInViewed.value = true
                }
            }
        }
    }

    fun addMovieToInterestedCollection(movieRVModel:MovieRVModel,idCollection:Long,dir:File,bitmap:Bitmap){
        //добавляю текущий фильм в коллекцию фильмов, которыми интересовался
        viewModelScope.launch(Dispatchers.IO) {
            addMovieToInterestedCollectionUsecase.addMovieToInterested(
                movieRVModel,
                idCollection,
                dir,
                bitmap
            )//ProfileFragment.ID_INTERESTED_COLLECTION)
        }
    }

}