package org.sniffsnirr.skillcinema.usecases

import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.room.DatabaseRepository
import org.sniffsnirr.skillcinema.room.dbo.CollectionCountMovies
import javax.inject.Inject

//Usecase -  для работы BottomSheetDialogFragment со списком коллекций
// и меткой что фильм принадлежит или не принадлежит каждой коллекции из списка
@ActivityRetainedScoped
class GetCollectionAndCountMoviesWithMarkUsecase @Inject constructor(
    val databaseRepository: DatabaseRepository,
    private val getCollectionAndCountMoviesUsecase: GetCollectionAndCountMoviesUsecase,
    val getCountMovieInCollection: GetCountMovieInCollection
) {
    suspend fun getCollectionAndCountMoviesWithMark(movieId: Long): List<Pair<CollectionCountMovies, Boolean>> {
        val listCollectionCountMoviesWithMarks =
            mutableListOf<Pair<CollectionCountMovies, Boolean>>()
        val listCollectionCountMovies =
            getCollectionAndCountMoviesUsecase.getCollectionAndCountMovies()
        listCollectionCountMovies.map { collection ->
            listCollectionCountMoviesWithMarks.add(
                Pair(
                    collection,
                    getCountMovieInCollection.isAlreadyExist(movieId, collection.id)
                )
            )
        }
        listCollectionCountMoviesWithMarks.add(Pair(listCollectionCountMovies.last(), false))
        return listCollectionCountMoviesWithMarks
    }

}