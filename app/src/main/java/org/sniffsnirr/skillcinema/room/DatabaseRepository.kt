package org.sniffsnirr.skillcinema.room


import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.sniffsnirr.skillcinema.room.dao.CollectionDAO
import org.sniffsnirr.skillcinema.room.dao.MovieDAO
import org.sniffsnirr.skillcinema.room.dbo.CollectionDBO
import org.sniffsnirr.skillcinema.room.dbo.MovieDBO
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import org.sniffsnirr.skillcinema.ui.profile.ProfileFragment
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseRepository @Inject constructor(
    private val collectionDAO: CollectionDAO,
    val movieDao: MovieDAO
) {

    val context = Dispatchers.IO + SupervisorJob()
    val scope = CoroutineScope(context)

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

    suspend fun deleteMovieFromCollection(kinopoiskId: Long,collectionId: Long){
        movieDao.deleteMovieByKinopoiskIdAndCollectionId(kinopoiskId,collectionId)
    }

    suspend fun clearCollection(collectionId: Long){
        movieDao.deleteAllMoviesByCollectionId(collectionId)
    }

// fun MovieRVModel.isViewed():Unit {
//        if (this.kinopoiskId != null) {
//            scope.launch(Dispatchers.IO)
//            {
//                if (getCountMovieInCollection(
//                        kinopoiskId.toLong(),
//                        ProfileFragment.ID_VIEWED_COLLECTION,
//                    )>0
//                ) {
//                    this@isViewed.viewed=true
//                } else this@isViewed.viewed=false
//            }
//        }
//    }
}