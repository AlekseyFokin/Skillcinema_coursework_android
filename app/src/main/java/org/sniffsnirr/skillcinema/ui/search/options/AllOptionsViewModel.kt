package org.sniffsnirr.skillcinema.ui.search.options

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.sniffsnirr.skillcinema.di.DaggerAllOptionsViewModelComponent
import org.sniffsnirr.skillcinema.entities.compilations.countriesandgenres.Country
import org.sniffsnirr.skillcinema.entities.compilations.countriesandgenres.Genre
import org.sniffsnirr.skillcinema.ui.search.QueryParams
import org.sniffsnirr.skillcinema.ui.search.SearchState
import org.sniffsnirr.skillcinema.ui.search.SearchViewModel.Companion.ALL_TYPE
import org.sniffsnirr.skillcinema.ui.search.SearchViewModel.Companion.DEFAULT_COUNTRY
import org.sniffsnirr.skillcinema.ui.search.SearchViewModel.Companion.DEFAULT_END_PERIOD
import org.sniffsnirr.skillcinema.ui.search.SearchViewModel.Companion.DEFAULT_END_RATE
import org.sniffsnirr.skillcinema.ui.search.SearchViewModel.Companion.DEFAULT_GENRE
import org.sniffsnirr.skillcinema.ui.search.SearchViewModel.Companion.DEFAULT_KEYWORD
import org.sniffsnirr.skillcinema.ui.search.SearchViewModel.Companion.DEFAULT_ONLY_UNVIEWED
import org.sniffsnirr.skillcinema.ui.search.SearchViewModel.Companion.DEFAULT_START_PERIOD
import org.sniffsnirr.skillcinema.ui.search.SearchViewModel.Companion.DEFAULT_START_RATE
import org.sniffsnirr.skillcinema.ui.search.SearchViewModel.Companion.SORT_DEFAULT
import org.sniffsnirr.skillcinema.usecases.GetCountriesAndGenresUsecase
import javax.inject.Inject

class AllOptionsViewModel @Inject constructor(
) :
    ViewModel() {
    @Inject
    lateinit var getCountriesAndGenresUsecase: GetCountriesAndGenresUsecase//инжекция в поле

    init {
        DaggerAllOptionsViewModelComponent.create().inject(this)// инжекция через компонент
        getCountriesAndGenres()
    }

    private val _queryParams =
        MutableStateFlow(
            QueryParams(
                DEFAULT_COUNTRY, DEFAULT_GENRE, SORT_DEFAULT, ALL_TYPE,
                DEFAULT_START_RATE, DEFAULT_END_RATE, DEFAULT_START_PERIOD, DEFAULT_END_PERIOD,
                DEFAULT_ONLY_UNVIEWED, DEFAULT_KEYWORD
            )
        )// состояние поиска страны
    val queryParams = _queryParams.asStateFlow()

    private val _searchCountryState =
        MutableStateFlow<SearchState>(SearchState.AvailableSearch) // состояние поиска страны

    private val _searchCountryString =
        MutableStateFlow("") // строка поиска страны
    private val searchCountryString = _searchCountryString.asStateFlow()

    private val _searchGenreState =
        MutableStateFlow<SearchState>(SearchState.AvailableSearch) // состояние поиска жанра

    private val _searchGenreString =
        MutableStateFlow("") // строка поиска жанра
    private val searchGenreString = _searchGenreString.asStateFlow()

    private val _type = MutableStateFlow(ALL_TYPE) // фильтр тип: фильм, сериал или оба
    val type = _type.asStateFlow()

    private val _country = MutableStateFlow<Country?>(DEFAULT_COUNTRY) // фильтр страна
    val country = _country.asStateFlow()

    private val _countries = MutableStateFlow(emptyList<Country>()) // список стран
    val countries = _countries.asStateFlow()

    private lateinit var firstCountriesList: List<Country>

    private val _genre = MutableStateFlow<Genre?>(DEFAULT_GENRE) // фильтр жанр
    val genre = _genre.asStateFlow()

    private val _genres = MutableStateFlow(emptyList<Genre>()) // список жанров
    val genres = _genres.asStateFlow()

    private lateinit var firstGenresList: List<Genre>

    private val _startPeriod = MutableStateFlow<Int?>(DEFAULT_START_PERIOD) // фильтр старт периода
    val startPeriod = _startPeriod.asStateFlow()

    private val _endPeriod = MutableStateFlow<Int?>(DEFAULT_END_PERIOD) // фильтр конец периода
    val endPeriod = _endPeriod.asStateFlow()

    private val _startRate = MutableStateFlow<Float?>(DEFAULT_START_RATE) // фильтр старт рейтинг
    val startRate = _startRate.asStateFlow()

    private val _endRate = MutableStateFlow<Float?>(DEFAULT_END_RATE) // фильтр конец рейтинг
    val endRate = _endRate.asStateFlow()

    private val _onlyUnviewed =
        MutableStateFlow(DEFAULT_ONLY_UNVIEWED) // фильтр скрывать просмотенные или нет
    val onlyUnviewed = _onlyUnviewed.asStateFlow()

    private val _sort = MutableStateFlow(SORT_DEFAULT) // вид сортировки
    val sort = _sort.asStateFlow()

    private val _error = Channel<Boolean>() // для передачи ошибки соединения с сервисом поиска
    val error = _error.receiveAsFlow()

    private var searchingCountryJob: Job? = null // текущий job поиска страны
    private var searchingGenreJob: Job? = null // текущий job поиска жанра

    private var previusSearchingCountryString = ""
    private var previusSearchingGenreString = ""

    fun setType(newType: String) {
        _type.value = newType
        _queryParams.value.type=newType
    }

    fun setCountry(newCountry: Country?) {
        _country.value = newCountry
        _queryParams.value.country=newCountry?.id
    }

    fun setGenre(newGenre: Genre?) {
        _genre.value = newGenre
        _queryParams.value.genre=newGenre?.id
    }

    fun setStartPeriod(newStartPeriod: Int?) {
        _startPeriod.value = newStartPeriod
        _queryParams.value.yearFrom=newStartPeriod
    }

    fun setEndPeriod(newEndPeriod: Int?) {
        _endPeriod.value = newEndPeriod
        _queryParams.value.yearTo=newEndPeriod
    }


    fun setStartRate(newStartRate: Float?) {
        _startRate.value = newStartRate
        _queryParams.value.ratingFrom=newStartRate
    }

    fun setEndRate(newEndRate: Float?) {
        _endRate.value = newEndRate
        _queryParams.value.ratingTo=newEndRate
    }

    fun setOnlyUnviewed(newOnlyUnviewed: Boolean) {
        _onlyUnviewed.value = newOnlyUnviewed
        _queryParams.value.onlyUnviewed=newOnlyUnviewed
    }

    fun setSort(sortMode: String) {
        _sort.value = sortMode
        _queryParams.value.order=sortMode
    }

fun setQueryParams(newQueryParams:QueryParams){
    _queryParams.value=newQueryParams


}

    private fun getCountriesAndGenres() {
        viewModelScope.launch(Dispatchers.IO) {// загрузка жанров и стран из Api
            kotlin.runCatching {
                getCountriesAndGenresUsecase.getCountriesAndGenres()
            }.fold(
                onSuccess = {
                    _countries.value = it.first
                    firstCountriesList = it.first
                    _genres.value = it.second
                    firstGenresList = it.second
                   },
                onFailure = {
                    Log.d("Error", "Загрузка жанров и стран из Api: ${it.message}")
                    _error.send(true)  // показывать диалог с ошибкой - где onFailure
                }
            )
        }
    }

    fun setCountrySearchString(string: String) {// поступление новой строки фильтрации стран
        previusSearchingCountryString = _searchCountryString.value
        _searchCountryString.value = string

        //если список уже пустой то и нечего сортировать
        if (previusSearchingCountryString.length < _searchCountryString.value.length && _searchCountryState.value == SearchState.EmptyData) {
            return
        }
        searchingCountryJob?.cancel()
        searchingCountryJob = searchCountryString.debounce(300).onEach {
            _searchCountryState.value = SearchState.Loading
            val outCountryList = firstCountriesList.filter { country ->
                country.country.lowercase().startsWith(_searchCountryString.value)
            }
            if (outCountryList.isEmpty()) {
                _searchCountryState.value = SearchState.EmptyData
            } else {
                _searchCountryState.value = SearchState.AvailableSearch
            }
            _countries.value = outCountryList
        }.launchIn(viewModelScope)
    }

    fun setGenreSearchString(string: String) {// поступление новой строки фильтрации жанров
        previusSearchingGenreString = _searchGenreString.value
        _searchGenreString.value = string

        if (previusSearchingGenreString.length < _searchGenreString.value.length && _searchGenreState.value == SearchState.EmptyData) {
            return
        }
        searchingGenreJob?.cancel()
        searchingGenreJob = searchGenreString.debounce(300).onEach {
            _searchGenreState.value = SearchState.Loading
            val outGenreList = firstGenresList.filter { genre ->
                genre.genre.lowercase().startsWith(_searchGenreString.value)
            }
            if (outGenreList.isEmpty()) {
                _searchGenreState.value = SearchState.EmptyData
            } else {
                _searchGenreState.value = SearchState.AvailableSearch
            }
            _genres.value = outGenreList
        }.launchIn(viewModelScope)
    }
}