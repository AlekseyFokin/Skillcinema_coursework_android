package org.sniffsnirr.skillcinema.usecases

import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.entities.images.Image
import org.sniffsnirr.skillcinema.restrepository.KinopoiskRepository
import javax.inject.Inject

// получение изображений для фильма
@ActivityRetainedScoped
class GetImagesUsecase @Inject constructor(
    val kinopoiskRepository: KinopoiskRepository
) {
    suspend fun getImages(movieId: Int,imageType:String=IMAGE_TYPE_DEFAULT): List<Image> { // получения списка изображений по типу
        val listOfImages = kinopoiskRepository.getImages(movieId, imageType)
        return listOfImages
    }

    suspend fun getNumberOfImegesByType(movieId:Int,listOfImageTypes:Set<String>):Map<String,Int>// получение количества изображений по типу
    {
        val numberOfImegesByType= mutableMapOf<String,Int>()
        listOfImageTypes.map { imageType->
            numberOfImegesByType.put(imageType,kinopoiskRepository.getNumberOfImages(movieId, imageType))
         }
return numberOfImegesByType
    }

    companion object {
        const val IMAGE_TYPE_DEFAULT = "STILL"
    }
}


