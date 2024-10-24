package org.sniffsnirr.skillcinema.usecases

import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.restrepository.KinopoiskRepository
import org.sniffsnirr.skillcinema.room.DatabaseRepository
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import java.util.Locale
import javax.inject.Inject

@ActivityRetainedScoped
class GetMoviesFromDBByCollectionUsecase @Inject constructor(
    val databaseRepository: DatabaseRepository,
    val kinopoiskRepository: KinopoiskRepository,
    val reduction: Reduction,
    val decideMovieRVmodelIsViewedOrNot: DecideMovieRVmodelIsViewedOrNot
) {
    private suspend fun getMoviesDBOFromDb(collectionId: Long) = //получение списока kinopoisk_id из БД
        databaseRepository.getMovieDboByCollectionId(collectionId)

    private suspend fun loadMovieRVModelFromAPI(idMovie: Int): MovieRVModel {// получение данных по конкретному фильму и конвертация  к MovieRVModel
        val onlyOneMovie = kinopoiskRepository.getOneMovie(idMovie)
        val movieRVModel= MovieRVModel(
            onlyOneMovie.kinopoiskId,
            onlyOneMovie.posterUrlPreview,
            reduction.stringReduction(onlyOneMovie.nameRu, 17),
            reduction.arrayReduction(onlyOneMovie.genres.map { it.genre }, 20, 2),
            "  ${String.format(Locale.US, "%.1f", onlyOneMovie.ratingKinopoisk)}  ",
            false, false, null
        )
        decideMovieRVmodelIsViewedOrNot.setMovieRVmodelViewed(movieRVModel)
        return movieRVModel
    }

    suspend fun getMoviesByCollection(collectionId: Long): List<MovieRVModel> { // получение набора данных для rv
        val viewedMoviesDBO = getMoviesDBOFromDb(collectionId)
        val viewedMoviesRVModel = mutableListOf<MovieRVModel>()
        if (!viewedMoviesDBO.isNullOrEmpty()) {
            viewedMoviesDBO.map { movie -> viewedMoviesRVModel.add(loadMovieRVModelFromAPI(movie.id_kinopoisk.toInt())) }
          }
        return viewedMoviesRVModel
    }
}