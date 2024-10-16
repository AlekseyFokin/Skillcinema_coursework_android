package org.sniffsnirr.skillcinema.room


import android.util.Log
import org.sniffsnirr.skillcinema.room.dao.CollectionDAO
import org.sniffsnirr.skillcinema.room.dao.MovieDAO
import org.sniffsnirr.skillcinema.room.dbo.CollectionDBO
import org.sniffsnirr.skillcinema.room.dbo.MovieDBO
import javax.inject.Inject

class DatabaseRepository @Inject constructor(
    private val collectionDAO: CollectionDAO,
    private val movieDao: MovieDAO
) {
    suspend fun addNewCollection(collection: CollectionDBO) = collectionDAO.insert(collection)
    suspend fun deleteCollection(collection: CollectionDBO) = collectionDAO.delete(collection)
    suspend fun getAllCollections() = collectionDAO.getAllCollections()
    suspend fun getDeletableCollections() = collectionDAO.getDeletableCollections()
    suspend fun getEmbeddedCollections() = collectionDAO.getEmbeddedCollections()

    suspend fun addNewMovie(movie: MovieDBO) = movieDao.insert(movie)
    suspend fun deleteMovie(movie: MovieDBO) = movieDao.delete(movie)
    suspend fun getMovieDboByKinopoiskId(kinopoiskId: Long) =
        movieDao.getMoviesDboByKinopoiskId(kinopoiskId)

    suspend fun getMoviesDboByCollectionIdLimited(collectionId: Long) =
        movieDao.getMoviesDboByCollectionIdLimited(collectionId)

    suspend fun getCountMovieDboByCollectionId(collectionId: Long) =
        movieDao.getCountMoviesDboByCollectionId(collectionId)

    suspend fun getMovieDboByCollectionId(collectionId: Long) =
        movieDao.getMoviesDboByCollectionId(collectionId)

    suspend fun getMovieDboByKinopoiskIdAndCollectionId(
        kinopoiskId: Long,
        collectionId: Long
    ) = movieDao.getMovieDboByKinopoiskIdAndCollectionId(kinopoiskId, collectionId)

    suspend fun insertNewMovie(kinopoiskId: Long,collectionId: Long) {
        movieDao.addOnlyNewMovieToCollection(kinopoiskId,collectionId)
        Log.d("Insert","repository-insertNewMovie")
    }

    suspend fun getCountMovieInCollection(kinopoiskId: Long,collectionId: Long):Int{
        return movieDao.getCountMovieDboByKinopoiskIdAndCollectionId(kinopoiskId,collectionId)
    }
}