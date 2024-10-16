package org.sniffsnirr.skillcinema.usecases

import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.restrepository.KinopoiskRepository
import org.sniffsnirr.skillcinema.room.DatabaseRepository
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import java.util.Locale
import javax.inject.Inject

@ActivityRetainedScoped
class GetViewedMoviesUsecase @Inject constructor(
    val databaseRepository: DatabaseRepository,
    val kinopoiskRepository: KinopoiskRepository,
    val reduction: Reduction
) {

    private suspend fun getViewedMoviesDBOFromDb(collectionId: Long) = //получение списока kinopoisk_id из БД
        databaseRepository.getMoviesDboByCollectionIdLimited(collectionId)

    private suspend fun loadMovieRVModelFromAPI(idMovie: Int): MovieRVModel {// получение данных по конкретному фильму и конвертация  к MovieRVModel
        val onlyOneMovie = kinopoiskRepository.getOneMovie(idMovie)
        return MovieRVModel(
            onlyOneMovie.kinopoiskId,
            onlyOneMovie.posterUrlPreview,
            reduction.stringReduction(onlyOneMovie.nameRu, 17),
            reduction.arrayReduction(onlyOneMovie.genres.map { it.genre }, 20, 2),
            "  ${String.format(Locale.US, "%.1f", onlyOneMovie.ratingKinopoisk)}  ",
            false, false, null
        )
    }

    suspend fun getViewedMovies(collectionId: Long): List<MovieRVModel> { // получение набора данных для rv
        val viewedMoviesDBO = getViewedMoviesDBOFromDb(collectionId)
        val viewedMoviesRVModel = mutableListOf<MovieRVModel>()
        if (!viewedMoviesDBO.isNullOrEmpty()) {
            viewedMoviesDBO.map { movie -> viewedMoviesRVModel.add(loadMovieRVModelFromAPI(movie.id_kinopoisk.toInt())) }
            viewedMoviesRVModel.add(  //добавляю кнопку
                MovieRVModel(
                    isButton = true,
                    categoryDescription = Triple("VIEWED_MOVIES", null, null)
                )
            )
        }
        return viewedMoviesRVModel
    }
}