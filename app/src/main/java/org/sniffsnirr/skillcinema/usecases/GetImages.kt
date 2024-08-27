package org.sniffsnirr.skillcinema.usecases

import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.entities.images.Image
import org.sniffsnirr.skillcinema.restrepository.KinopoiskRepository
import javax.inject.Inject

// получение изображений для фильма
@ActivityRetainedScoped
class GetImages @Inject constructor(
    val kinopoiskRepository: KinopoiskRepository
) {
    suspend fun getImages(movieId: Int): List<Image> {
        val listOfImages = kinopoiskRepository.getImages(movieId, IMAGE_TYPE_DEFAULT)
        return listOfImages
    }

    companion object {
        const val IMAGE_TYPE_DEFAULT = "STILL"
    }
}


