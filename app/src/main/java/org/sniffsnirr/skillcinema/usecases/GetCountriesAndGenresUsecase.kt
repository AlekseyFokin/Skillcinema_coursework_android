package org.sniffsnirr.skillcinema.usecases

import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.entities.compilations.countriesandgenres.Country
import org.sniffsnirr.skillcinema.entities.compilations.countriesandgenres.Genre
import org.sniffsnirr.skillcinema.restrepository.KinopoiskRepository
import javax.inject.Inject

@ActivityRetainedScoped
class GetCountriesAndGenresUsecase @Inject constructor(
    val kinopoiskRepository: KinopoiskRepository) {

    suspend fun getCountriesAndGenres(): Pair<List<Country>, List<Genre>> {// получение списка стран и жанров
        val countriesAndGenres = kinopoiskRepository.getCountryAndGenre()
        return Pair(countriesAndGenres.countries,countriesAndGenres.genres)
    }
}