package org.sniffsnirr.skillcinema.usecases

import android.util.Log
import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.room.DatabaseRepository
import javax.inject.Inject

@ActivityRetainedScoped
class DeleteAllMoviesFromCollectionUsecase @Inject constructor(
    val getMoviesFromDBByCollectionUsecase: GetMoviesFromDBByCollectionUsecase,
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