package org.sniffsnirr.skillcinema.usecases

import android.util.Log
import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.room.DatabaseRepository
import javax.inject.Inject

@ActivityRetainedScoped
class DeleteAllMoviesFromCollectionUsecase @Inject constructor(val databaseRepository: DatabaseRepository){
    suspend fun deleteAllMovieFromCollection(collectionId: Long,){
        databaseRepository.clearCollection(collectionId)
        Log.d("Insert","InsertNewMovieUsecase-addNewMovie")
    }
}