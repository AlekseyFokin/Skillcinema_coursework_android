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

class RoomInjectionModule {
    @Provides
    @Singleton
    fun provideDB(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "skillcinema.db"
        )// создание бд и заполнение начальными значениями коллекций
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    db.run {
                        beginTransaction()
                        try {
                            execSQL("insert into collection values (${ProfileFragment.ID_FAVORITE_COLLECTION},'Любимые',1)")
                            execSQL("insert into collection values (${ProfileFragment.ID_WANT_TO_SEE_COLLECTION},'Хочу посмотреть',1)")
                            execSQL("insert into collection values (${ProfileFragment.ID_VIEWED_COLLECTION},'Просмотрено',1)")
                            execSQL("insert into collection values (${ProfileFragment.ID_INTERESTED_COLLECTION},'Вам было интересно',1)")
                            setTransactionSuccessful()
                        } finally {
                            endTransaction()
                        }
                    }
                }
            })
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideCollectionDao(db: AppDatabase) = db.collectionDao()

    @Provides
    @Singleton
    fun provideMovieDao(db: AppDatabase) = db.movieDao()

//    @Provides
//    @Singleton
//    fun provideDatabaseRepository(
//        collectionDao: CollectionDAO,
//        movieDao: MovieDAO
//    ): DatabaseRepository {
//        return DatabaseRepository(collectionDao, movieDao)
//    }
//
//    @Provides
//    @Singleton
//    fun providedecideMovieRVmodelIsViewedOrNotUsecase(databaseRepository: DatabaseRepository): DecideMovieRVmodelIsViewedOrNotUsecase {
//        return DecideMovieRVmodelIsViewedOrNotUsecase(databaseRepository)
//    }


}