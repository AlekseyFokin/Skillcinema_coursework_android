package org.sniffsnirr.skillcinema.room

import org.sniffsnirr.skillcinema.room.dao.CollectionDAO
import org.sniffsnirr.skillcinema.room.dao.MovieDAO
import org.sniffsnirr.skillcinema.room.dbo.CollectionDBO
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import javax.inject.Inject
import javax.inject.Singleton

// репозиторий работы с БД
@Singleton
class DatabaseRepository @Inject constructor(
    private val collectionDAO: CollectionDAO,
    private val movieDao: MovieDAO
) {
    //получить ограниченное количество фильмов из коллекции
    suspend fun getMoviesDboByCollectionIdLimited(collectionId: Long) =
        movieDao.getMoviesDboByCollectionIdLimited(collectionId)

    // количество фильмов в конкретной коллекции
    fun getCountMovieDboByCollectionId(collectionId: Long) =
        movieDao.getCountMoviesDboByCollectionId(collectionId)
    
    //все фильмы из конкретной коллекции
    fun getMovieDboByCollectionId(collectionId: Long) =
        movieDao.getMoviesDboByCollectionId(collectionId)

    // получение фильма по коллекции и id_kinopoisk
    suspend fun getMovieDboByKinopoiskIdAndCollectionId(
        kinopoiskId: Long,
        collectionId: Long
    ) = movieDao.getMovieDboByKinopoiskIdAndCollectionId(kinopoiskId, collectionId)

    // вставка фильма в коллекцию
    suspend fun insertNewMovie(movieRVModel: MovieRVModel, collectionId: Long) {
        movieDao.addOnlyNewMovieToCollection(movieRVModel, collectionId)
    }

    // проверка - есть ли фильм в коллекции
    suspend fun getCountMovieInCollection(kinopoiskId: Long, collectionId: Long): Int {
        return movieDao.getCountMovieDboByKinopoiskIdAndCollectionId(kinopoiskId, collectionId)
    }

    // удаление фильма из коллекции
    suspend fun deleteMovieFromCollection(kinopoiskId: Long, collectionId: Long) {
        movieDao.deleteMovieByKinopoiskIdAndCollectionId(kinopoiskId, collectionId)
    }

    // список коллекций с количеством фильмов
    suspend fun getCollectionAndCountMovies() =
        collectionDAO.getCollectionCountMovies()

    //удаление коллекции
    suspend fun deleteCollectionById(collection: CollectionDBO) {
        collectionDAO.deleteOnlyDeletableCollection(collection)
    }

    //получение коллекции по id
    suspend fun getCollectionById(collectionId: Long): CollectionDBO {
        return collectionDAO.getCollectionById(collectionId)
    }

    //добавление коллекции
    suspend fun insertCollection(collectionName: String) {
        collectionDAO.insertCollection(collectionName)
    }

}