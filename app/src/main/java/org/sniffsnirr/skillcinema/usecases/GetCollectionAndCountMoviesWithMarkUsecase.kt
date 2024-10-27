package org.sniffsnirr.skillcinema.usecases

import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.room.DatabaseRepository
import org.sniffsnirr.skillcinema.room.dbo.CollectionCountMovies
import javax.inject.Inject

@ActivityRetainedScoped
class GetCollectionAndCountMoviesWithMarkUsecase @Inject constructor(
    val databaseRepository: DatabaseRepository,
    val getCollectionAndCountMoviesUsecase: GetCollectionAndCountMoviesUsecase,
    val getCountMovieInCollection:GetCountMovieInCollection
) {

    suspend fun getCollectionAndCountMoviesWithMark(movieId:Long):List<Pair<CollectionCountMovies,Boolean>>{
        val ListCollectionCountMoviesWithMarks= mutableListOf<Pair<CollectionCountMovies,Boolean>>()
        val ListCollectionCountMovies =getCollectionAndCountMoviesUsecase.getCollectionAndCountMovies()
        ListCollectionCountMovies.map {
            collection->
            ListCollectionCountMoviesWithMarks.add(Pair(collection,getCountMovieInCollection.isAlreadyExist(movieId,collection.id)))
       }
        ListCollectionCountMoviesWithMarks.add(Pair(ListCollectionCountMovies.last(),false))
        return ListCollectionCountMoviesWithMarks
    }

}