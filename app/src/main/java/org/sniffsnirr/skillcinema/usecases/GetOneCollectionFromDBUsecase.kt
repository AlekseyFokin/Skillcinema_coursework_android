package org.sniffsnirr.skillcinema.usecases

import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.room.DatabaseRepository
import org.sniffsnirr.skillcinema.room.dbo.CollectionDBO
import javax.inject.Inject

@ActivityRetainedScoped
class GetOneCollectionFromDBUsecase @Inject constructor(
    val databaseRepository: DatabaseRepository) {
    suspend fun getCollectionById(collectionId:Long):CollectionDBO{
        return databaseRepository.getCollectionById(collectionId)
    }

}