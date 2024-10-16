package org.sniffsnirr.skillcinema.usecases

import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.room.DatabaseRepository
import javax.inject.Inject

@ActivityRetainedScoped
class InsertNewMovieUsecase @Inject constructor(val databaseRepository: DatabaseRepository) {
    suspend fun addNewMovie(collectionId: Long,kinopoiskId: Long){
        databaseRepository.insertNewMovie(collectionId,kinopoiskId)

    }
}