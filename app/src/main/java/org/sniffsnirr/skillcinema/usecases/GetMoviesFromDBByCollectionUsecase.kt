package org.sniffsnirr.skillcinema.usecases

import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.room.DatabaseRepository
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import javax.inject.Inject

//Usecase -списка фильмов из коллекции
@ActivityRetainedScoped
class GetMoviesFromDBByCollectionUsecase @Inject constructor(
    val databaseRepository: DatabaseRepository,
    val decideMovieRVmodelIsViewedOrNotUsecase: DecideMovieRVmodelIsViewedOrNotUsecase
) {
    private fun getMoviesDBOFromDb(collectionId: Long) =
        //получение списока kinopoisk_id из БД
        databaseRepository.getMovieDboByCollectionId(collectionId)

    suspend fun getMoviesByCollection(collectionId: Long): List<MovieRVModel> { // получение набора данных для rv
        val moviesDBO = getMoviesDBOFromDb(collectionId)
        val moviesRVModel = mutableListOf<MovieRVModel>()
        if (!moviesDBO.isNullOrEmpty()) {
            moviesDBO.map { movie ->
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
                decideMovieRVmodelIsViewedOrNotUsecase.setMovieRVmodelViewed(movieRVModel)
                moviesRVModel.add(movieRVModel)
            }
        }
        return moviesRVModel
    }
}