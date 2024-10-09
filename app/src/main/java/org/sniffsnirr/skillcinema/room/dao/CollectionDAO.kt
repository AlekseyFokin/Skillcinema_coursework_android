package org.sniffsnirr.skillcinema.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.sniffsnirr.skillcinema.room.dbo.CollectionDBO


@Dao
interface CollectionDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(collection: CollectionDBO)

    @Delete
    suspend fun delete(collection: CollectionDBO)

    @Query ("Select * from collection")
    suspend fun getAllCollections():List<CollectionDBO>

    @Query ("Select * from collection where embedded=0")
    suspend fun getDeletableCollections():List<CollectionDBO>

    @Query ("Select * from collection where embedded=1")
    suspend fun getEmbeddedCollections():List<CollectionDBO>
}