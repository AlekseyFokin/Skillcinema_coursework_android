package org.sniffsnirr.skillcinema.usecases

import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.room.DatabaseRepository
import org.sniffsnirr.skillcinema.room.dbo.CollectionCountMovies
import org.sniffsnirr.skillcinema.room.dbo.CollectionDBO
import javax.inject.Inject

@ActivityRetainedScoped
class DeleteCollectionUsecase  @Inject constructor(val databaseRepository: DatabaseRepository){
    suspend  fun deleteCollection(collection:CollectionCountMovies){
        val collectionDBO=CollectionDBO(collection.id,collection.name,collection.embedded)
        databaseRepository.deleteCollectionById(collectionDBO)
    }
}