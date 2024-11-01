package org.sniffsnirr.skillcinema.room.dao

import androidx.room.Dao
import androidx.room.Delete

import androidx.room.Query
import androidx.room.Transaction
import org.sniffsnirr.skillcinema.room.dbo.MovieDBO
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import org.sniffsnirr.skillcinema.ui.profile.ProfileFragment

@Dao
interface MovieDAO {

    //   @Insert(onConflict = OnConflictStrategy.IGNORE)
    //   suspend fun insert(movie: MovieDBO)

    @Delete
    suspend fun delete(movie: MovieDBO)//удаление одного кина

//    @Query("Select * from movie where id_kinopoisk=:kinopoiskId")
//    fun getMoviesDboByKinopoiskId(kinopoiskId: Long): List<MovieDBO>?

    @Query("Select * from movie where id_set=:collectionId order by id desc")//список фильмов в коллекции отсортированный по убыванию id
    fun getMoviesDboByCollectionId(collectionId: Long): List<MovieDBO>?

    @Query("Select count(id) from movie where id_set=:collectionId") // количество фильмов в одной коллекции
    fun getCountMoviesDboByCollectionId(collectionId: Long): Int

    @Query("Select * from movie where id_set=:collectionId limit ${ProfileFragment.LIMIT_FOR_RV}")// показ на profileFragment(есть лимит)
    suspend fun getMoviesDboByCollectionIdLimited(collectionId: Long): List<MovieDBO>?

    @Query("Select * from movie where (id_kinopoisk=:kinopoiskId) and (id_set=:collectionId)")// получение конкретного фильма из конкретной коллекции
    suspend fun getMovieDboByKinopoiskIdAndCollectionId(
        kinopoiskId: Long,
        collectionId: Long
    ): MovieDBO?

    @Query("Select count(id) from movie where (id_kinopoisk=:kinopoiskId) and (id_set=:collectionId)")// проверка существования фильма в коллекции
    suspend fun getCountMovieDboByKinopoiskIdAndCollectionId(
        kinopoiskId: Long,
        collectionId: Long
    ): Int

//    @Query("Select count(id) from movie where (id_set=:collectionId)")
//    suspend fun getCountMovieDboByCollectionId(collectionId: Long): Long

    // добавление фильма в конкретную коллекцию
    @Query("Insert into movie (id_set,id_kinopoisk,poster,name,genre,rate) values (:collectionId,:kinopoiskId,:poster,:name,:genre,:rate)")
    suspend fun addNewMovieToCollection(
        kinopoiskId: Long,
        collectionId: Long,
        poster: String,
        name: String,
        genre: String,
        rate: String
    )

    @Transaction// добавление фильма в конкретную коллекцию с проверкой нет ли этого фильма уже в этой коллекции
    suspend fun addOnlyNewMovieToCollection(movieRVModel: MovieRVModel, collectionId: Long) {
        if (getCountMovieDboByKinopoiskIdAndCollectionId(
                movieRVModel.kinopoiskId!!.toLong(),
                collectionId
            ) < 1
        ) {
            addNewMovieToCollection(
                movieRVModel.kinopoiskId.toLong(),
                collectionId,
                movieRVModel.imageUrl,
                movieRVModel.movieName,
                movieRVModel.movieGenre,
                movieRVModel.rate
            )
        }
    }

    // удаление конкретного фильма из конкретной коллекции
    @Query("Delete from movie where ((id_kinopoisk=:kinopoiskId) and (id_set=:collectionId))")
    suspend fun deleteMovieByKinopoiskIdAndCollectionId(kinopoiskId: Long, collectionId: Long)

    // удаление всех фильмов из конкретной коллекции
    @Query("Delete from movie where (id_set=:collectionId)")
    suspend fun deleteAllMoviesByCollectionId(collectionId: Long)

//    @Transaction// вставка фильма в коллекцию фильмов которыми интересовался - с проверкой на дубли и удаление фильма самого старого в коллекции
//    suspend fun insertMovieToInterested(
//        movieRvModel: MovieRVModel,
//        idInterestedCollection: Long
//    ) {//проверка дублей
//        if (getCountMovieDboByKinopoiskIdAndCollectionId(
//                movieRvModel.kinopoiskId!!.toLong(),
//                idInterestedCollection
//            ) < 1
//        ) {
//            //проверка лимита  если лимит превышен - удаление первого
//            val moviesInCollection = getMoviesDboByCollectionId(idInterestedCollection)
//
//            if (moviesInCollection?.size == ProfileFragment.LIMIT_FOR_INTERESTED_COLLECTION) {
//                delete(moviesInCollection.last())
//            }
//            // вставка нового фильма
//            addNewMovieToCollection(
//                movieRvModel.kinopoiskId.toLong(),
//                idInterestedCollection,
//                movieRvModel.imageUrl,
//                movieRvModel.movieName,
//                movieRvModel.movieGenre,
//                movieRvModel.rate
//            )
//        }
//    }
}