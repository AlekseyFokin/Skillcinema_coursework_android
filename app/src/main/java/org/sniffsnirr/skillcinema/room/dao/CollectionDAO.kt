package org.sniffsnirr.skillcinema.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.sniffsnirr.skillcinema.room.dbo.CollectionDBO
import org.sniffsnirr.skillcinema.room.dbo.MovieDBO

@Dao
interface CollectionDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(collection: CollectionDBO)

    @Delete
    suspend fun delete(collection: CollectionDBO)

    @Query ("Select * from collection where embedded=0")
    suspend fun getDeletableCollections()
}