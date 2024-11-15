package org.sniffsnirr.skillcinema.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.sniffsnirr.skillcinema.restrepository.KinopoiskRepository
import org.sniffsnirr.skillcinema.usecases.DecideMovieRVmodelIsViewedOrNotUsecase
import org.sniffsnirr.skillcinema.usecases.ReductionUsecase
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    var kinopoiskRepository: KinopoiskRepository,
    var reductionUsecase: ReductionUsecase,
    val decideMovieRVmodelIsViewedOrNotUsecase: DecideMovieRVmodelIsViewedOrNotUsecase
) :
    ViewModel() {

    // начальные параметры поиска
    private val _queryParams =
        MutableStateFlow(
            QueryParams(
                DEFAULT_COUNTRY, DEFAULT_GENRE, SORT_DEFAULT, ALL_TYPE,
                DEFAULT_START_RATE, DEFAULT_END_RATE, DEFAULT_START_PERIOD, DEFAULT_END_PERIOD,
                DEFAULT_ONLY_UNVIEWED, DEFAULT_KEYWORD
            )
        )
    val queryParams = _queryParams.asStateFlow()

    // пагинированные фильтрованные данные для отображения
    private var pagingSource: SearchResultPagingSource? = null
        get() {
            if (field == null || field?.invalid == true) {
                field = SearchResultPagingSource(
                    kinopoiskRepository,
                    reductionUsecase,
                    _queryParams.value,
                    decideMovieRVmodelIsViewedOrNotUsecase
                )
            }
            return field
        }

    val filtredflow = Pager(
        PagingConfig(pageSize = 10)
    ) {
        pagingSource!!
    }.flow.cachedIn(viewModelScope)  // представление во фрагменте

    private val _searchKeywordString = MutableStateFlow("") // фильтр фильмов по строке

    private val _searchStatus =
        MutableStateFlow("") // статус поиска

    fun setSearchMovieString(string: String) {// поступление новой строки фильтрации названий, актеров, режиссеров
        _searchKeywordString.value = string
        var tempQueryParams = _queryParams.value
        tempQueryParams = tempQueryParams.copy(keyword = string)
        _queryParams.value = tempQueryParams
        pagingSource?.invalidate()
    }

    fun setQueryParams(newQueryParams: QueryParams) {
        _queryParams.value = newQueryParams
        pagingSource?.invalidate()
    }

    companion object {
        const val ALL_TYPE = "ALL"
        const val MOVIE_ONLY_TYPE = "FILM"
        const val SERIAL_ONLY_TYPE = "TV_SERIES"

        val DEFAULT_COUNTRY = null
        val DEFAULT_GENRE = null
        val DEFAULT_START_PERIOD = null
        val DEFAULT_END_PERIOD = null
        val DEFAULT_START_RATE = null
        val DEFAULT_END_RATE = null
        const val DEFAULT_ONLY_UNVIEWED = false
        val DEFAULT_KEYWORD = null

        const val SORT_DEFAULT = "RATING"
        const val SORT_DATE = "YEAR"
        const val SORT_POP = "NUM_VOTE"
        const val SORT_RATE = "RATING"
    }
}