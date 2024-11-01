package org.sniffsnirr.skillcinema.usecases

import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.room.DatabaseRepository
import org.sniffsnirr.skillcinema.room.dbo.MovieDBO
import javax.inject.Inject


// Usecase - получение одного филима из коллекции БД
@ActivityRetainedScoped
class GetOneMovieFromDBByCollectionUsecase @Inject constructor(val databaseRepository: DatabaseRepository){
    suspend fun getMovie(kinopoiskId:Long,collectionId:Long):MovieDBO?{
    return databaseRepository.getMovieDboByKinopoiskIdAndCollectionId(kinopoiskId,collectionId)
    }
}