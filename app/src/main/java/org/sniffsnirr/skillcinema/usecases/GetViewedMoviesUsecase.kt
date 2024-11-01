package org.sniffsnirr.skillcinema.usecases

import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.room.DatabaseRepository
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import javax.inject.Inject

//Usecase - получение просмотенных фильмов из БД
@ActivityRetainedScoped
class GetViewedMoviesUsecase @Inject constructor(
    val databaseRepository: DatabaseRepository,
    val reduction: Reduction,
    val decideMovieRVmodelIsViewedOrNot: DecideMovieRVmodelIsViewedOrNot
) {

    private suspend fun getViewedMoviesDBOFromDb(collectionId: Long) =
        //получение списка kinopoisk_id из БД
        databaseRepository.getMoviesDboByCollectionIdLimited(collectionId)

    suspend fun getViewedMovies(collectionId: Long): List<MovieRVModel> { // получение набора данных для rv
        val viewedMoviesDBO = getViewedMoviesDBOFromDb(collectionId)
        val viewedMoviesRVModel = mutableListOf<MovieRVModel>()
        if (!viewedMoviesDBO.isNullOrEmpty()) {
            viewedMoviesDBO.map { movie ->
                val movieRVModel = MovieRVModel(
                    movie.id_kinopoisk.toInt(),
                    movie.poster,
                    movie.name,
                    movie.genre,
                    movie.rate,
                    false,
                    false,
                    null
                )
                decideMovieRVmodelIsViewedOrNot.setMovieRVmodelViewed(movieRVModel)
                viewedMoviesRVModel.add(movieRVModel)
            }
        }
        return viewedMoviesRVModel
    }
}