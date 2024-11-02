package org.sniffsnirr.skillcinema.ui.onemovie.gallery

import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadResult.Page
import androidx.paging.PagingState
import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.entities.images.Image
import org.sniffsnirr.skillcinema.restrepository.KinopoiskRepository

// класс загрузки пагинирующей галереи
@ActivityRetainedScoped
class GalleryPagingSource(
    val kinopoiskRepository: KinopoiskRepository,
    val idMovie: Int,
    private val imageType: String
) : PagingSource<Int, Image>() {
    override fun getRefreshKey(state: PagingState<Int, Image>): Int = FIRST_PAGE

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Image> {
        val page = params.key ?: FIRST_PAGE
        return kotlin.runCatching {
            kinopoiskRepository.getImages(idMovie, imageType, page)
        }.fold(
            onSuccess = {
                Page(
                    data = it,
                    prevKey = null,
                    nextKey = if (it.isEmpty()) null else page + 1
                )
            },
            onFailure = { LoadResult.Error(it) }
        )
    }

    companion object {
        private const val FIRST_PAGE = 1
    }
}