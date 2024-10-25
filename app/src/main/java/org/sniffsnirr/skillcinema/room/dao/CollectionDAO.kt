package org.sniffsnirr.skillcinema.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import org.sniffsnirr.skillcinema.room.dbo.CollectionCountMovies
import org.sniffsnirr.skillcinema.room.dbo.CollectionDBO


@Dao
interface CollectionDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(collection: CollectionDBO)

    @Delete
    suspend fun delete(collection: CollectionDBO)

    @Query("Select * from collection")
    suspend fun getAllCollections(): List<CollectionDBO>

    @Query("Select * from collection where embedded=0")
    suspend fun getDeletableCollections(): List<CollectionDBO>

    @Query("Select * from collection where embedded=1")
    suspend fun getEmbeddedCollections(): List<CollectionDBO>

    @Query("select collection.id, collection.name, collection.embedded, count(movie.id) as countMovies from collection left join movie on collection.id=movie.id_set group by (collection.id) ")
    suspend fun getCollectionCountMovies(): List<CollectionCountMovies>

    @Query("delete from collection where id=:collectionId")
    suspend fun deleteCollectionById(collectionId: Int)

    @Transaction
    suspend fun deleteOnlyDeletableCollection(collection: CollectionDBO) {
        if (getDeletableCollections().contains(collection)) {
            delete(collection)
        }
    }
    @Query("select * from collection where id=:collectionId")
    suspend fun getCollectionById(collectionId:Long):CollectionDBO

    @Query("insert into collection (name,embedded) values(:collectionName,0)")
    suspend fun insertCollection(collectionName:String)
}