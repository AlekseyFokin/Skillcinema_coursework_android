package org.sniffsnirr.skillcinema.room

import androidx.room.Database
import androidx.room.RoomDatabase
import org.sniffsnirr.skillcinema.room.dao.CollectionDAO
import org.sniffsnirr.skillcinema.room.dao.MovieDAO
import org.sniffsnirr.skillcinema.room.dbo.MovieDBO
import org.sniffsnirr.skillcinema.room.dbo.CollectionDBO

@Database(entities = [CollectionDBO::class, MovieDBO::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun collectionDao(): CollectionDAO
    abstract fun movieDao(): MovieDAO

}
