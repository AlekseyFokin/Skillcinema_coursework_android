package org.sniffsnirr.skillcinema.usecases

import android.graphics.Bitmap
import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.room.DatabaseRepository
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import org.sniffsnirr.skillcinema.ui.profile.ProfileFragment
import java.io.File
import javax.inject.Inject

//Usecase добавления в коллекцию фильмов которые просматривал пользователь.
// На коллекцию установлен лимит, при превышении которого старые записи удаляются и вставляются новые,
// есть проверка нв дубли
@ActivityRetainedScoped
class AddMovieToInterestedCollectionUsecase @Inject constructor(
    val databaseRepository: DatabaseRepository,
    val deleteMovieFromCollectionUsecase: DeleteMovieFromCollectionUsecase,
    val insertNewMovieToCollectionUsecase: InsertNewMovieToCollectionUsecase
) {
    suspend fun addMovieToInterested(
        movieRVModel: MovieRVModel,
        idInterestedCollection: Long,
        dir: File,
        bitmap: Bitmap
    ) {
        //проверка дублей

        if (databaseRepository.getCountMovieInCollection(
                movieRVModel.kinopoiskId!!.toLong(),
                idInterestedCollection
            ) < 1
        ) {
            //проверка лимита  если лимит превышен - удаление последнего
            val moviesInCollection =
                databaseRepository.getMovieDboByCollectionId(idInterestedCollection)

            if (moviesInCollection?.size == ProfileFragment.LIMIT_FOR_INTERESTED_COLLECTION) {
                val movieRVModelForDelete = MovieRVModel(
                    moviesInCollection.last().id_kinopoisk.toInt(),
                    moviesInCollection.last().poster,
                    moviesInCollection.last().name,
                    moviesInCollection.last().genre,
                    moviesInCollection.last().rate,
                    false,
                    false,
                    null
                )
                deleteMovieFromCollectionUsecase.deleteMovieFromCollection(
                    movieRVModelForDelete,
                    idInterestedCollection
                )
            }
            insertNewMovieToCollectionUsecase.addNewMovie(
                movieRVModel,
                idInterestedCollection,
                dir,
                bitmap
            )
        }
    }
}