package org.sniffsnirr.skillcinema.ui.onemovie.gallery

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sniffsnirr.skillcinema.entities.images.Image
import org.sniffsnirr.skillcinema.restrepository.KinopoiskRepository
import org.sniffsnirr.skillcinema.usecases.GetImagesUsecase
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val getImagesUsecase: GetImagesUsecase,
    val kinopoiskRepository: KinopoiskRepository
) : ViewModel() {

    var idMovie = 0

    private val _numberOfImagesByType = MutableStateFlow<Map<String, Int>>(emptyMap())
    var numberOfImagesByType = _numberOfImagesByType.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    var isLoading = _isLoading.asStateFlow()


    fun getImages(imageType: String): Flow<PagingData<Image>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { GalleryPagingSource(kinopoiskRepository, idMovie, imageType) }
        ).flow
    }

    fun getNumberOfImagesByType(imageTypesList: Set<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                _isLoading.value = true
                getImagesUsecase.getNumberOfImegesByType(idMovie, imageTypesList)
            }.fold(
                onSuccess = {
                    _numberOfImagesByType.value = it
                },
                onFailure = { Log.d("MoviesGallery", it.message ?: "") }
            )
            _isLoading.value = false
        }
    }

}