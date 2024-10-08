package org.sniffsnirr.skillcinema.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.sniffsnirr.skillcinema.room.dbo.MovieDBO

@Dao
interface MovieDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(movie: MovieDBO)

    @Delete
    suspend fun delete(movie: MovieDBO)

    @Query("Select * from movie where id_kinopoisk=:kinopoiskId")
    suspend fun getMovieDboByKinopoiskId(kinopoiskId: Long): List<MovieDBO>?

}