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
class InsertNewMovieToCollectionUsecase @Inject constructor(val databaseRepository: DatabaseRepository) {
    suspend fun addNewMovie(movieRVModel: MovieRVModel, collectionId: Long, dir: File, bitmap: Bitmap){
        val uuid = UUID.randomUUID() //генератор имени файла
        movieRVModel.imageUrl="${uuid}.jpg"

        //сохранить в директорию файл постер
        val file = File(dir, "${uuid}.jpg")
        try {
            val fOut: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
            fOut.close()
        } catch (e: Exception) {
            Log.d("SaveFileError","${e.printStackTrace()}")
        }

        //сохраняю в БД
        databaseRepository.insertNewMovie(movieRVModel,collectionId)
        Log.d("Insert","InsertNewMovieUsecase-addNewMovie")
    }
}