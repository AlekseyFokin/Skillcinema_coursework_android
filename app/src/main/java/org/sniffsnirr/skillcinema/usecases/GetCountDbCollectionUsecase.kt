package org.sniffsnirr.skillcinema.usecases

import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.room.DatabaseRepository
import javax.inject.Inject

@ActivityRetainedScoped
class GetCountDbCollectionUsecase @Inject constructor(val databaseRepository: DatabaseRepository) {

   suspend fun getCountCollection(collectionId: Long):Int{
    return databaseRepository.getCountMovieDboByCollectionId(collectionId)
   }

}