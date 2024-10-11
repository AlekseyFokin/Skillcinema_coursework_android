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
import org.sniffsnirr.skillcinema.ui.profile.ProfileFragment
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
                    db.run {
                        // Notice non-ui thread is here
                        beginTransaction()
                        try {
                            execSQL("insert into collection (name, embedded) values ('Любимые',${ProfileFragment.ID_FAVORITE_COLLECTION})")
                            execSQL("insert into collection (name, embedded) values ('Хочу посмотреть',${ProfileFragment.ID_SCHEDULED_COLLECTION})")
                            execSQL("insert into collection (name, embedded) values ('Просмотрено',${ProfileFragment.ID_VIEWED_COLLECTION})")
                            execSQL("insert into collection (name, embedded) values ('Вам было интересно',${ProfileFragment.ID_INTERESTED_COLLECTION})")
                            setTransactionSuccessful()
                        } finally {
                            endTransaction()
                        }
                    }
                }
            }).build()

    @Provides
    @Singleton
    fun provideCollectionDao(db: AppDatabase) = db.collectionDao()

    @Provides
    @Singleton
    fun provideMovieDao(db: AppDatabase) = db.movieDao()

}