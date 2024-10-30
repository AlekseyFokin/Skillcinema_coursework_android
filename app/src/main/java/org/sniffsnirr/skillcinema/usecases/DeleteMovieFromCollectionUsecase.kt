package org.sniffsnirr.skillcinema.usecases

import android.util.Log
import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.room.DatabaseRepository
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import java.io.File
import javax.inject.Inject

@ActivityRetainedScoped
class DeleteMovieFromCollectionUsecase @Inject constructor(
    val databaseRepository: DatabaseRepository,
    val getOneMovieFromDBByCollectionUsecase: GetOneMovieFromDBByCollectionUsecase,
    val getCountMovieInCollection:GetCountMovieInCollection
) {

    suspend fun deleteMovieFromCollection(movieRVModel: MovieRVModel, collectionId: Long) {
        if(getCountMovieInCollection.isAlreadyExist(movieRVModel.kinopoiskId!!.toLong(),collectionId)) {
            //удаляю файл
            val movieForDeleteFromCollection = getOneMovieFromDBByCollectionUsecase.getMovie(
                movieRVModel.kinopoiskId!!.toLong(),
                collectionId
            )
            try {
                //  val file=File(dir, movieForDeleteFromCollection?.poster!!)
                val file = File(movieForDeleteFromCollection?.poster!!)
                file.delete()
            } catch (e: Exception) {
                Log.d("DeleteFileError", "${e.printStackTrace()}")
            }
            //удаляю из БД
            databaseRepository.deleteMovieFromCollection(
                movieRVModel.kinopoiskId!!.toLong(),
                collectionId
            )
        }
    }
}