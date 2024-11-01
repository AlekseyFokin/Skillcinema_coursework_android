package org.sniffsnirr.skillcinema.usecases

import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

// Usecase удаления всех фильмов из коллекции
@ActivityRetainedScoped
class DeleteAllMoviesFromCollectionUsecase @Inject constructor(
    private val getMoviesFromDBByCollectionUsecase: GetMoviesFromDBByCollectionUsecase,
    val deleteMovieFromCollectionUsecase: DeleteMovieFromCollectionUsecase
) {
    suspend fun deleteAllMovieFromCollection(collectionId: Long) {
        val allMoviesFromCollectionForDelete =
            getMoviesFromDBByCollectionUsecase.getMoviesByCollection(collectionId)
        allMoviesFromCollectionForDelete.map { movieRVModel ->
            deleteMovieFromCollectionUsecase.deleteMovieFromCollection(
                movieRVModel,
                collectionId
            )
        }
    }
}