package org.sniffsnirr.skillcinema.usecases

import android.util.Log
import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.room.DatabaseRepository
import javax.inject.Inject

@ActivityRetainedScoped
class InsertNewMovieUsecase @Inject constructor(val databaseRepository: DatabaseRepository) {
    suspend fun addNewMovie(kinopoiskId: Long,collectionId: Long,){
        databaseRepository.insertNewMovie(kinopoiskId,collectionId)
        Log.d("Insert","InsertNewMovieUsecase-addNewMovie")

    }
}