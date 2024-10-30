package org.sniffsnirr.skillcinema.usecases

import android.graphics.Bitmap
import android.util.Log
import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.room.DatabaseRepository
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.UUID
import javax.inject.Inject

@ActivityRetainedScoped
class InsertNewMovieToCollectionUsecase @Inject constructor(val databaseRepository: DatabaseRepository,
    val getCountMovieInCollection:GetCountMovieInCollection) {
    suspend fun addNewMovie(movieRVModel: MovieRVModel, collectionId: Long, dir: File, bitmap: Bitmap) {
        if (!getCountMovieInCollection.isAlreadyExist(
                movieRVModel.kinopoiskId!!.toLong(),
                collectionId
            )
        ) {
            Log.d("Insert", "Зашел сюда")
            val uuid = UUID.randomUUID() //генератор имени файла
            val file = File(dir, "${uuid}.jpg")
            //movieRVModel.imageUrl="${uuid}.jpg"
            movieRVModel.imageUrl = file.absolutePath
            //сохранить в директорию файл постер
            //val file = File(dir, "${uuid}.jpg")
            Log.d("Insert", "Дошел до сохранения в в файл")
            try {
                val fOut: OutputStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                fOut.close()
            } catch (e: Exception) {
                Log.d("SaveFileError", "${e.printStackTrace()}")
            }

            //сохраняю в БД
            Log.d("Insert", "Дошел до сохранения в бд")
            databaseRepository.insertNewMovie(movieRVModel, collectionId)
            Log.d("Insert", "InsertNewMovieUsecase-addNewMovie")
        }
    }
}