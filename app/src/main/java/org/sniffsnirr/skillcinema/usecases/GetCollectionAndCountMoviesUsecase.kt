package org.sniffsnirr.skillcinema.usecases

import org.sniffsnirr.skillcinema.room.DatabaseRepository
import org.sniffsnirr.skillcinema.room.dbo.CollectionCountMovies
import javax.inject.Inject

class GetCollectionAndCountMoviesUsecase @Inject constructor(val databaseRepository: DatabaseRepository) {
    suspend fun getCollectionAndCountMovies():List<CollectionCountMovies>{
        return databaseRepository.getCollectionAndCountMovies()
    }
}