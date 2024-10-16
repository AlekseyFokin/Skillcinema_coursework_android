package org.sniffsnirr.skillcinema.usecases

import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.room.DatabaseRepository
import javax.inject.Inject

@ActivityRetainedScoped
class GetCountMovieInCollection @Inject constructor(val databaseRepository: DatabaseRepository) {
    suspend fun isAlreadyExist(kinopoiskId:Long, collectionId:Long):Boolean{
        if (databaseRepository.getCountMovieInCollection(kinopoiskId,collectionId)<1)
        {return false}
        else {return true}
    }
}