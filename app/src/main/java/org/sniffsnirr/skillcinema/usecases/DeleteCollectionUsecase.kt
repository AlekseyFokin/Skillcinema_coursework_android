package org.sniffsnirr.skillcinema.usecases

import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.room.DatabaseRepository
import org.sniffsnirr.skillcinema.room.dbo.CollectionCountMovies
import org.sniffsnirr.skillcinema.room.dbo.CollectionDBO
import javax.inject.Inject

@ActivityRetainedScoped
class DeleteCollectionUsecase @Inject constructor(
    val databaseRepository: DatabaseRepository,
    val getMoviesFromDBByCollectionUsecase: GetMoviesFromDBByCollectionUsecase,
    val deleteMovieFromCollectionUsecase: DeleteMovieFromCollectionUsecase
) {
    suspend fun deleteCollection(collection: CollectionCountMovies) {
        // для удаления коллекции удалить все фильмы и файлы которые с ними связаны, а затем коллекцию
        val allMoviesFromCollectionForDelete =
            getMoviesFromDBByCollectionUsecase.getMoviesByCollection(collection.id)
        allMoviesFromCollectionForDelete.map { movieRVModel ->
            deleteMovieFromCollectionUsecase.deleteMovieFromCollection(
                movieRVModel,
                collection.id
            )
        }
        val collectionDBO = CollectionDBO(collection.id, collection.name, collection.embedded)
        databaseRepository.deleteCollectionById(collectionDBO)
    }
}