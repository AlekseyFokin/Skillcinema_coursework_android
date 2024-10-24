package org.sniffsnirr.skillcinema.usecases

import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.room.DatabaseRepository
import javax.inject.Inject

@ActivityRetainedScoped
class AddMovieToInterestedCollectionUsecase @Inject constructor(val databaseRepository: DatabaseRepository) {
    suspend fun addMovieToInterested(kinopoiskId:Long,idInterestedCollection:Long)
    {
        databaseRepository.addMovieToInterestedCollection(kinopoiskId,idInterestedCollection)
    }
}