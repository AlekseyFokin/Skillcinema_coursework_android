package org.sniffsnirr.skillcinema.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import org.sniffsnirr.skillcinema.room.dbo.MovieDBO
import org.sniffsnirr.skillcinema.ui.profile.ProfileFragment

@Dao
interface MovieDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(movie: MovieDBO)

    @Delete
    suspend fun delete(movie: MovieDBO)

    @Query("Select * from movie where id_kinopoisk=:kinopoiskId")
    fun getMoviesDboByKinopoiskId(kinopoiskId: Long): List<MovieDBO>?

    @Query("Select * from movie where id_set=:collectionId order by id desc")
    fun getMoviesDboByCollectionId(collectionId: Long): List<MovieDBO>?

    @Query("Select count(id) from movie where id_set=:collectionId")
    fun getCountMoviesDboByCollectionId(collectionId: Long): Int

    @Query("Select * from movie where id_set=:collectionId limit ${ProfileFragment.LIMIT_FOR_RV}")// показ на profileFragment
    suspend fun getMoviesDboByCollectionIdLimited(collectionId: Long): List<MovieDBO>?

    @Query("Select * from movie where (id_kinopoisk=:kinopoiskId) and (id_set=:collectionId)")
    suspend fun getMovieDboByKinopoiskIdAndCollectionId(
        kinopoiskId: Long,
        collectionId: Long
    ): MovieDBO?

    @Query("Select count(id) from movie where (id_kinopoisk=:kinopoiskId) and (id_set=:collectionId)")// проверка существования фильма в коллекции
    suspend fun getCountMovieDboByKinopoiskIdAndCollectionId(
        kinopoiskId: Long,
        collectionId: Long
    ): Int

    @Query("Select count(id) from movie where (id_set=:collectionId)")
    suspend fun getCountMovieDboByCollectionId(collectionId: Long): Long

    @Query("Insert into movie (id_set,id_kinopoisk) values (:collectionId,:kinopoiskId)")
    suspend fun addNewMovieToCollection(kinopoiskId: Long, collectionId: Long)

    @Transaction
    suspend fun addOnlyNewMovieToCollection(kinopoiskId: Long, collectionId: Long) {
        if (getCountMovieDboByKinopoiskIdAndCollectionId(kinopoiskId, collectionId) < 1)
            addNewMovieToCollection(kinopoiskId, collectionId)
    }

    @Query("Delete from movie where ((id_kinopoisk=:kinopoiskId) and (id_set=:collectionId))")
    suspend fun deleteMovieByKinopoiskIdAndCollectionId(kinopoiskId: Long, collectionId: Long)

    @Query("Delete from movie where (id_set=:collectionId)")
    suspend fun deleteAllMoviesByCollectionId(collectionId: Long)

    @Transaction
    suspend fun insertMovieToInterested(kinopoiskId:Long,idInterestedCollection:Long)
    {//проверка дублей

        if(getCountMovieDboByKinopoiskIdAndCollectionId(kinopoiskId,idInterestedCollection)<1)
        {
            //проверка лимита  если лимит превышен - удаление первого
          val moviesInCollection=getMoviesDboByCollectionId(idInterestedCollection)

            if (moviesInCollection?.size==ProfileFragment.LIMIT_FOR_INTERESTED_COLLECTION)
            {
                delete(moviesInCollection.last())
            }
            // вставка нового фильма
            addNewMovieToCollection(kinopoiskId,idInterestedCollection)
        }



    }
}