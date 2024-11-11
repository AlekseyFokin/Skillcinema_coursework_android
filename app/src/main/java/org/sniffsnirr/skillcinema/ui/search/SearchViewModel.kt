package org.sniffsnirr.skillcinema.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.sniffsnirr.skillcinema.restrepository.KinopoiskRepository
import org.sniffsnirr.skillcinema.usecases.DecideMovieRVmodelIsViewedOrNotUsecase
import org.sniffsnirr.skillcinema.usecases.ReductionUsecase
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(var kinopoiskRepository: KinopoiskRepository,var reductionUsecase: ReductionUsecase,decideMovieRVmodelIsViewedOrNotUsecase: DecideMovieRVmodelIsViewedOrNotUsecase
) :
    ViewModel() {

    private val _queryParams =
        MutableStateFlow<QueryParams>(
            QueryParams(
                DEFAULT_COUNTRY, DEFAULT_GENRE, SORT_DEFAULT, ALL_TYPE,
                DEFAULT_START_RATE, DEFAULT_END_RATE, DEFAULT_START_PERIOD, DEFAULT_END_PERIOD,
                DEFAULT_ONLY_UNVIEWED, DEFAULT_KEYWORD
            )
        )// состояние поиска страны
    val queryParams = _queryParams.asStateFlow()

    val pagedMovies = _queryParams.flatMapMerge{ queryParams ->
        Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = {
                SearchResultPagingSource(
                    kinopoiskRepository,
                    reductionUsecase,
                    queryParams,
                    decideMovieRVmodelIsViewedOrNotUsecase
                )
            }
        ).flow.cachedIn(viewModelScope)
    }



  //  private val _searchKeywordState =
  //      MutableStateFlow<SearchState>(SearchState.AvailableSearch) // состояние поиска фильма
  //  val searchKeywordState = _searchKeywordState.asStateFlow()

    private val _searchKeywordString = MutableStateFlow("") // фильтр фильмов по строке
    val searchKeywordString = _searchKeywordString.asStateFlow()

    private var searchingMovieJob: Job? = null // текущий job поиска фильма


    private var previusSearchingMovieString=""


    fun setSearchMovieString(string: String) {// поступление новой строки фильтрации названий, актеров, режиссеров
        previusSearchingMovieString = _searchKeywordString.value
        _searchKeywordString.value = string

        //_queryParams.value.keyword=string
        var tempQueryParams=_queryParams.value
        tempQueryParams=tempQueryParams.copy(keyword =string )
        viewModelScope.launch(Dispatchers.IO) {
        _queryParams.emit(tempQueryParams)}
    }

    fun setQueryParams(newQueryParams:QueryParams){
        _queryParams.value=newQueryParams
    }

//    fun onSearchMovieString() {// новая итерация фильтрации  названий, актеров, режиссеров
//        searchingMovieJob?.cancel()
//        //если список уже пустой то и нечего сортировать
//        if (previusSearchingMovieString.length < _searchKeywordString.value.length && _searchKeywordState.value == SearchState.EmptyData) {
//            return
//        }
//        searchingMovieJob = searchMovieString.debounce(300).onEach {
//            _searchMovieState.value = SearchState.Loading
//            val outMovieList = firstMovieList.filter { genre ->
//                genre.genre.lowercase().startsWith(_searchGenreString.value)
//            }
//            if (outGenreList.size == 0) {
//                _searchGenreState.value = SearchState.EmptyData
//            } else {
//                _searchGenreState.value = SearchState.AvailableSearch
//            }
//            _genres.value = outGenreList
//        }.launchIn(viewModelScope)
//    }


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
        val DEFAULT_ONLY_UNVIEWED = false
        val DEFAULT_KEYWORD = null

        const val SORT_DEFAULT = "RATING"
        const val SORT_DATE = "YEAR"
        const val SORT_POP = "NUM_VOTE"
        const val SORT_RATE = "RATING"
    }
}