package org.sniffsnirr.skillcinema.usecases

import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.room.DatabaseRepository
import javax.inject.Inject

@ActivityRetainedScoped
class CreateCollectionUsecase @Inject constructor(
    val databaseRepository: DatabaseRepository){
   suspend  fun insertCollection(nameCollection:String)
    {
        databaseRepository.insertCollection(nameCollection)
    }
}