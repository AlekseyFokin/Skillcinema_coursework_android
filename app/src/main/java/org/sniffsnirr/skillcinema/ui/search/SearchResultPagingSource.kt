package org.sniffsnirr.skillcinema.ui.search

import androidx.paging.PagingSource
import androidx.paging.PagingState
import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.restrepository.KinopoiskRepository
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import org.sniffsnirr.skillcinema.usecases.DecideMovieRVmodelIsViewedOrNotUsecase
import org.sniffsnirr.skillcinema.usecases.ReductionUsecase

// класс загрузки пагинирующей коллекции фильмов на основе фильтра с приведением к MovieRVModel типу
@ActivityRetainedScoped
class SearchResultPagingSource(
    val kinopoiskRepository: KinopoiskRepository,
    val reductionUsecase: ReductionUsecase,
    val country: Int?,
    val genre: Int?,
    val order: String,
    val type: String,
    val ratingFrom: Float?,
    val ratingTo: Float?,
    val yearFrom: Int?,
    val yearTo: Int? ,
    val keyword: String?,
    val page: Int,
    val decideMovieRVmodelIsViewedOrNotUsecase: DecideMovieRVmodelIsViewedOrNotUsecase
): PagingSource<Int, MovieRVModel>()  {

    override fun getRefreshKey(state: PagingState<Int, MovieRVModel>): Int? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieRVModel> {
        TODO("Not yet implemented")
    }
}