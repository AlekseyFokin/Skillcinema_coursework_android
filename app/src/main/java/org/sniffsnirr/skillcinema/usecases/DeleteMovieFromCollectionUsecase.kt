package org.sniffsnirr.skillcinema.usecases

import android.util.Log
import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.room.DatabaseRepository
import javax.inject.Inject

@ActivityRetainedScoped
class DeleteMovieFromCollectionUsecase @Inject constructor(val databaseRepository: DatabaseRepository) {

    suspend fun deleteMovieFromCollection(kinopoiskId: Long,collectionId: Long,){
        databaseRepository.deleteMovieFromCollection(kinopoiskId,collectionId)
        Log.d("Insert","InsertNewMovieUsecase-addNewMovie")
    }
}