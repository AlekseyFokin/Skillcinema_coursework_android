package org.sniffsnirr.skillcinema.usecases

import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.room.DatabaseRepository
import javax.inject.Inject

//Usecase - проверка есть ли конкретный фильм в конкретной коллекции
@ActivityRetainedScoped
class GetCountMovieInCollectionUsecase @Inject constructor(val databaseRepository: DatabaseRepository) {
    suspend fun isAlreadyExist(kinopoiskId: Long, collectionId: Long): Boolean {
        return databaseRepository.getCountMovieInCollection(kinopoiskId, collectionId) >= 1
    }
}