package org.sniffsnirr.skillcinema.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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

    @Query("Select * from movie where id_set=:collectionId")
    fun getMoviesDboByCollectionId(collectionId: Long): List<MovieDBO>?

    @Query("Select * from movie where id_set=:collectionId limit ${ProfileFragment.LIMIT_FOR_RV}")
    suspend fun getMoviesDboByCollectionIdLimited(collectionId: Long): List<MovieDBO>?

    @Query("Select * from movie where (id_kinopoisk=:kinopoiskId) and (id_set=:collectionId)")
    suspend fun getMovieDboByKinopoiskIdAndCollectionId(kinopoiskId: Long,collectionId:Long): MovieDBO?

    @Query("Select count(id) from movie where (id_set=:collectionId)")
    suspend fun getCountMovieDboByCollectionId(collectionId:Long): Long

}