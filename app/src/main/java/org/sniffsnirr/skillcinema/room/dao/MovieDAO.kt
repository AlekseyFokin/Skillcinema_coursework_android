package org.sniffsnirr.skillcinema.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.sniffsnirr.skillcinema.room.dbo.MovieDBO

@Dao
interface MovieDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(movie: MovieDBO)

    @Delete
    suspend fun delete(movie: MovieDBO)

    @Query("Select * from movie where id_kinopoisk=:kinopoiskId")
    suspend fun getMoviesDboByKinopoiskId(kinopoiskId: Long): Flow<List<MovieDBO>>?

    @Query("Select * from movie where id_set=:collectionId")
    suspend fun getMoviesDboByCollectionId(collectionId: Long): Flow<List<MovieDBO>>?

    @Query("Select * from movie where (id_kinopoisk=:kinopoiskId) and (id_set=:collectionId)")
    suspend fun getMovieDboByKinopoiskIdAndCollectionId(kinopoiskId: Long,collectionId:Long): MovieDBO?

}