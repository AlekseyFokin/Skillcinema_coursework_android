package org.sniffsnirr.skillcinema.entities.compilations.countriesandgenres

// модель данных для получения списка стран и списка жанров
data class CountriesGenres(
    val countries: List<Country>,
    val genres: List<Genre>
)