package org.sniffsnirr.skillcinema.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _сountriesAndGenres = MutableStateFlow<Pair<List<Country>, List<Genre>>>(
        Pair(
            emptyList(),
            emptyList()
        )
    ) // страны и жанры
    val сountriesAndGenres = _сountriesAndGenres.asStateFlow()

    private val _searchState =
        MutableStateFlow<SearchState>(SearchState.SearchDone) // состояние поиска
    val searchState = _searchState.asStateFlow()

    private val _stringSearch = MutableStateFlow("") // фильтр по строке
    val stringSearch = _stringSearch.asStateFlow()

    private val _type = MutableStateFlow(ALL_TYPE) // фильтр тип: фильм, сериал или оба
    val type = _type.asStateFlow()

    private val _country = MutableStateFlow(DEFAULT_COUNTRY) // фильтр страна
    val country = _country.asStateFlow()

    private val _genre = MutableStateFlow(DEFAULT_GENRE) // фильтр жанр
    val genre = _genre.asStateFlow()

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

    fun setType(newType: String) {
        _type.value = newType
    }

    fun setCountry(newCountry: String) {
        _country.value = newCountry
    }

    fun setGenre(newGenre: String) {
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
                onSuccess = { _сountriesAndGenres.value = it
                            Log.d("загрузка и hilt module field","Загружено ${it.first.size}")},
                onFailure = { Log.d("ViewedList", it.message ?: "") }
            )
        }
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