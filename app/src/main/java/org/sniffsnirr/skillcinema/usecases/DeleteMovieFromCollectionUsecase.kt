package org.sniffsnirr.skillcinema.usecases

import android.util.Log
import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.room.DatabaseRepository
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import java.io.File
import javax.inject.Inject

@ActivityRetainedScoped
class DeleteMovieFromCollectionUsecase @Inject constructor(val databaseRepository: DatabaseRepository,
                                                           val getOneMovieFromDBByCollectionUsecase:GetOneMovieFromDBByCollectionUsecase) {

    suspend fun deleteMovieFromCollection(movieRVModel: MovieRVModel, collectionId: Long,dir: File){

        //удаляю файл
        val movieForDeleteFromCollection=getOneMovieFromDBByCollectionUsecase.getMovie(movieRVModel.kinopoiskId!!.toLong(),collectionId)
        try {
            val file=File(dir, movieForDeleteFromCollection?.poster!!)
            file.delete()
        } catch (e: Exception) {
            Log.d("DeleteFileError","${e.printStackTrace()}")
        }
        //удаляю из БД
        databaseRepository.deleteMovieFromCollection(movieRVModel.kinopoiskId!!.toLong(),collectionId)
    }
}