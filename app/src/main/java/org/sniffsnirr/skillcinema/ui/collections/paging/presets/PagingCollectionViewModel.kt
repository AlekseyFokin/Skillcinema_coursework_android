package org.sniffsnirr.skillcinema.ui.collections.paging.presets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import org.sniffsnirr.skillcinema.restrepository.KinopoiskRepository
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import org.sniffsnirr.skillcinema.usecases.DecideMovieRVmodelIsViewedOrNotUsecase
import org.sniffsnirr.skillcinema.usecases.ReductionUsecase
import javax.inject.Inject

@HiltViewModel
class PagingCollectionViewModel @Inject constructor(
    val kinopoiskRepository: KinopoiskRepository,
    val reductionUsecase: ReductionUsecase,
    val decideMovieRVmodelIsViewedOrNotUsecase: DecideMovieRVmodelIsViewedOrNotUsecase
) : ViewModel() {
    var collectionType: String = "" //для пердачи типа коллекции

    val pagedMovies: Flow<PagingData<MovieRVModel>> = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = { MoviePagingSource(kinopoiskRepository, reductionUsecase, collectionType,decideMovieRVmodelIsViewedOrNotUsecase) }
    ).flow.cachedIn(viewModelScope)
}