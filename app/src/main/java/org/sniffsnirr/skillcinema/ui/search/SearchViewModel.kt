package org.sniffsnirr.skillcinema.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.sniffsnirr.skillcinema.di.DaggerSearchViewModelComponent
import org.sniffsnirr.skillcinema.di.SearchViewModelComponent
import org.sniffsnirr.skillcinema.entities.compilations.countriesandgenres.Country
import org.sniffsnirr.skillcinema.entities.compilations.countriesandgenres.Genre
import org.sniffsnirr.skillcinema.usecases.GetCountriesAndGenresUsecase
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor() :
    ViewModel() {

    @Inject
    lateinit var getCountriesAndGenresUsecase: GetCountriesAndGenresUsecase//инжекция в поле

    init {
        DaggerSearchViewModelComponent.create().inject(this)// инжекция через компонент
        getCountriesAndGenres()
    }

    private val _searchCountryState =
        MutableStateFlow<SearchState>(SearchState.AvailableSearch) // состояние поиска страны
    val searchCountryState = _searchCountryState.asStateFlow()

    private val _searchCountryString =
        MutableStateFlow("") // строка поиска страны
    val searchCountryString = _searchCountryString.asStateFlow()

    private val _searchGenreState =
        MutableStateFlow<SearchState>(SearchState.AvailableSearch) // состояние поиска жанра
    val searchGenreState = _searchGenreState.asStateFlow()

    private val _searchGenreString =
        MutableStateFlow("") // строка поиска жанра
    val searchGenreString = _searchGenreString.asStateFlow()

    private val _searchMovieState =
        MutableStateFlow<SearchState>(SearchState.AvailableSearch) // состояние поиска фильма
    val searchMovieState = _searchMovieState.asStateFlow()

    private val _searchMovieString = MutableStateFlow("") // фильтр фильмов по строке
    val searchMovieString = _searchMovieString.asStateFlow()

    private val _type = MutableStateFlow(ALL_TYPE) // фильтр тип: фильм, сериал или оба
    val type = _type.asStateFlow()

    private val _country = MutableStateFlow<Country?>(null) // фильтр страна
    val country = _country.asStateFlow()

    private val _countries = MutableStateFlow(emptyList<Country>()) // список стран
    val countries = _countries.asStateFlow()

    lateinit var firstCountriesList: List<Country>

    private val _genre = MutableStateFlow<Genre?>(null) // фильтр жанр
    val genre = _genre.asStateFlow()

    private val _genres = MutableStateFlow(emptyList<Genre>()) // список жанров
    val genres = _genres.asStateFlow()

    lateinit var firstGenresList: List<Genre>

    private val _startPeriod = MutableStateFlow(DEFAULT_START_PERIOD) // фильтр старт периода
    val startPeriod = _startPeriod.asStateFlow()

    private val _endPeriod = MutableStateFlow(DEFAULT_END_PERIOD) // фильтр конец периода
    val endPeriod = _endPeriod.asStateFlow()

    private val _startRate = MutableStateFlow(DEFAULT_START_RATE) // фильтр старт рейтинг
    val startRate = _startRate.asStateFlow()

    private val _endRate = MutableStateFlow(DEFAULT_END_RATE) // фильтр конец рейтинг
    val endRate = _endRate.asStateFlow()

    private val _viewed = MutableStateFlow(DEFAULT_VIEWED) // фильтр скрывать просмотенные или нет
    val viewed = _viewed.asStateFlow()

    private val _sort = MutableStateFlow(SORT_DEFAULT) // вид сортировки
    val sort = _sort.asStateFlow()

    private var searchingCountryJob: Job? = null // текущий job поиска страны
    private var searchingGenreJob: Job? = null // текущий job поиска жанра
    private var searchingMovieJob: Job? = null // текущий job поиска фильма

    private var previusSearchingCountryString = ""
    private var previusSearchingGenreString = ""

    fun setType(newType: String) {
        _type.value = newType
    }

    fun setCountry(newCountry: Country) {
        _country.value = newCountry
    }

    fun setGenre(newGenre: Genre) {
        _genre.value = newGenre
    }

    fun setStartPeriod(newStartPeriod: Int) {
        _startPeriod.value = newStartPeriod
    }

    fun setEndPeriod(newEndPeriod: Int) {
        _endPeriod.value = newEndPeriod
    }

    fun setStartRate(newStartRate: Float) {
        _startRate.value = newStartRate
    }

    fun setEndRate(newEndRate: Float) {
        _endRate.value = newEndRate
    }

    fun setViewed(newViewed: Boolean) {
        _viewed.value = newViewed
    }

    fun setSort(sortMode: String) {
        _sort.value = sortMode
    }

    private fun getCountriesAndGenres() {
        viewModelScope.launch(Dispatchers.IO) {// загрузка жанров и стран
            kotlin.runCatching {
                getCountriesAndGenresUsecase.getCountriesAndGenres()
            }.fold(
                onSuccess = {
                    _countries.value = it.first
                    firstCountriesList = it.first
                    _genres.value = it.second
                    firstGenresList = it.second
                    Log.d("загрузка и hilt module field", "Загружено ${it.first.size}")
                },
                onFailure = { Log.d("ViewedList", it.message ?: "") }
            )
        }
    }

    fun setCountrySearchString(string: String) {// поступление новой строки фильтрации стран
        previusSearchingCountryString = _searchCountryString.value
        _searchCountryString.value = string
    }

    fun onChangeCountrySearchString() {// новая итерация фильтрации стран
        searchingCountryJob?.cancel()
        //если список уже пустой то и нечего сортировать
        if (previusSearchingCountryString.length < _searchCountryString.value.length && _searchCountryState.value == SearchState.EmptyData) {
            return
        }
        searchingCountryJob = searchCountryString.debounce(300).onEach {
            _searchCountryState.value = SearchState.Loading
            val outCountryList = firstCountriesList.filter { country ->
                country.country.lowercase().startsWith(_searchCountryString.value)
            }
            if (outCountryList.size == 0) {
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
    }

    fun onChangeGenreSearchString() {// новая итерация фильтрации стран
        searchingGenreJob?.cancel()
        //если список уже пустой то и нечего сортировать
        if (previusSearchingGenreString.length < _searchGenreString.value.length && _searchGenreState.value == SearchState.EmptyData) {
            return
        }
        searchingGenreJob = searchGenreString.debounce(300).onEach {
            _searchGenreState.value = SearchState.Loading
            val outGenreList = firstGenresList.filter { genre ->
                genre.genre.lowercase().startsWith(_searchGenreString.value)
            }
            if (outGenreList.size == 0) {
                _searchGenreState.value = SearchState.EmptyData
            } else {
                _searchGenreState.value = SearchState.AvailableSearch
            }
            _genres.value = outGenreList
        }.launchIn(viewModelScope)
    }


    companion object {
        const val ALL_TYPE = "ALL"
        const val MOVIE_ONLY_TYPE = "MOVIE_ONLY"
        const val SERIAL_ONLY_TYPE = "SERIAL_ONLY"
        const val DEFAULT_COUNTRY = "Россия"
        const val DEFAULT_GENRE = "Боевик"
        const val DEFAULT_START_PERIOD = 0
        const val DEFAULT_END_PERIOD = 0
        const val DEFAULT_START_RATE = 0.0f
        const val DEFAULT_END_RATE = 10.0f
        val DEFAULT_VIEWED = false

        const val SORT_DEFAULT = "SORT_DEFAULT"
        const val SORT_DATE = "SORT_DATE"
        const val SORT_POP = "SORT_POP"
        const val SORT_RATE = "SORT_RATE"
    }
}