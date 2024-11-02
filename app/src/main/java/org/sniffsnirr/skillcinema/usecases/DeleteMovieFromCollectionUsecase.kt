package org.sniffsnirr.skillcinema.usecases

import android.util.Log
import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.room.DatabaseRepository
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import java.io.File
import javax.inject.Inject

// Usecase - удаление файла из коллекции с удалением файла постера из папки с предварительной проверкой хранения файла в коллекции
@ActivityRetainedScoped
class DeleteMovieFromCollectionUsecase @Inject constructor(
    val databaseRepository: DatabaseRepository,
    private val getOneMovieFromDBByCollectionUsecase: GetOneMovieFromDBByCollectionUsecase,
    val getCountMovieInCollectionUsecase: GetCountMovieInCollectionUsecase
) {

    suspend fun deleteMovieFromCollection(movieRVModel: MovieRVModel, collectionId: Long) {
        if (getCountMovieInCollectionUsecase.isAlreadyExist(
                movieRVModel.kinopoiskId!!.toLong(),
                collectionId
            )
        ) {
            //удаляю файл
            val movieForDeleteFromCollection = getOneMovieFromDBByCollectionUsecase.getMovie(
                movieRVModel.kinopoiskId.toLong(),
                collectionId
            )
            try {
                val file = File(movieForDeleteFromCollection?.poster!!)
                file.delete()
            } catch (e: Exception) {
                Log.d("DeleteFileError", "${e.printStackTrace()}")
            }
            //удаляю из БД
            databaseRepository.deleteMovieFromCollection(
                movieRVModel.kinopoiskId.toLong(),
                collectionId
            )
        }
    }
}