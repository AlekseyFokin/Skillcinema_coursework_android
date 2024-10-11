package org.sniffsnirr.skillcinema.usecases

import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.restrepository.KinopoiskRepository
import org.sniffsnirr.skillcinema.room.DatabaseRepository
import org.sniffsnirr.skillcinema.room.dbo.MovieDBO
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import java.util.Locale
import javax.inject.Inject

@ActivityRetainedScoped
class GetViewedMoviesUsecase @Inject constructor(
    val databaseRepository: DatabaseRepository,
    val kinopoiskRepository: KinopoiskRepository,
    val reduction: Reduction
) {

    suspend fun getViewedMoviesDBOFromDb(collectionId: Long) =
        databaseRepository.getMoviesDboByCollectionIdLimited(collectionId)

    private suspend fun getMovieRVModel(idMovie: Int): MovieRVModel {// получение данных по конкретному фильму и конвертация  к MovieRVModel
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

    suspend fun getViewedMoviesFromAPI(collectionId: Long): List<MovieRVModel> {
        val viewedMoviesDBO = getViewedMoviesDBOFromDb(collectionId)
        val relatedMoviesRVModel = mutableListOf<MovieRVModel>()
        if (!viewedMoviesDBO.isNullOrEmpty()) {
            viewedMoviesDBO.map { movie -> relatedMoviesRVModel.add(getMovieRVModel(movie.id_kinopoisk.toInt())) }
        }
        return relatedMoviesRVModel
    }
}