package org.sniffsnirr.skillcinema.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.sniffsnirr.skillcinema.room.AppDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomInjectionModule {
    @Provides
    @Singleton
    fun provideDB(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, AppDatabase::class.java, "skillcinema.db")
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    // Populate the database with initial data
                    // For example:

                    val userDao = INSTANCE?.userDao()
                    userDao?.insert(User("John Doe"))
                    userDao?.insert(User("Jane Smith"))
                }
            })





                                                ioThread {
                                                getInstance(context).dataDao()
                                                .insert(PREPOPULATE_DATA)
                    }
                }
            }).build ()

    @Provides
    @Singleton
    fun provideCollectionDao(db: AppDatabase) = db.collectionDao()

    @Provides
    @Singleton
    fun provideMovieDao(db: AppDatabase) = db.movieDao()

}