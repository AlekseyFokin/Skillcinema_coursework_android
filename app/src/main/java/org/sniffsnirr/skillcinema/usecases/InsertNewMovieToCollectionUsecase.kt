package org.sniffsnirr.skillcinema.usecases

import android.graphics.Bitmap
import android.util.Log
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.sniffsnirr.skillcinema.room.DatabaseRepository
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.UUID
import javax.inject.Inject

//Usecase - добавление фильма в коллекцию с предварительной проверкой на дублирование и сохранением файла в директорию для постеров
@ActivityRetainedScoped
class InsertNewMovieToCollectionUsecase @Inject constructor(
    val databaseRepository: DatabaseRepository,
    private val getCountMovieInCollectionUsecase: GetCountMovieInCollectionUsecase
) {
    suspend fun addNewMovie(
        movieRVModel: MovieRVModel,
        collectionId: Long,
        dir: File,
        bitmap: Bitmap
    ) {
        if (!getCountMovieInCollectionUsecase.isAlreadyExist(
                movieRVModel.kinopoiskId!!.toLong(),
                collectionId
            )
        ) {
            val uuid = UUID.randomUUID() //генератор имени файла
            val file = File(dir, "${uuid}.jpg")
            movieRVModel.imageUrl = file.absolutePath
            //сохранить в директорию файл постер
            try {
                val fOut: OutputStream =
                    withContext(Dispatchers.IO) {
                        FileOutputStream(file)
                    }
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                withContext(Dispatchers.IO) {
                    fOut.close()
                }
            } catch (e: Exception) {
                Log.d("SaveFileError", "${e.printStackTrace()}")
            }
            //сохраняю в БД
            databaseRepository.insertNewMovie(movieRVModel, collectionId)
        }
    }
}