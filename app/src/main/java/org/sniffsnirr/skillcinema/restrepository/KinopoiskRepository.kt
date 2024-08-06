package org.sniffsnirr.skillcinema.restrepository

import kotlinx.coroutines.delay
import org.sniffsnirr.skillcinema.entities.compilations.CompilationsMovie
import org.sniffsnirr.skillcinema.entities.compilations.CompilationsMovieList
import org.sniffsnirr.skillcinema.entities.compilations.countriesandgenres.CountriesGenres
import org.sniffsnirr.skillcinema.entities.popular.PopularMovie
import org.sniffsnirr.skillcinema.entities.premiers.PremierMovie
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KinopoiskRepository @Inject constructor(val retrofitInstance: KinopoiskDataSource){
    val kinopoiskApi = retrofitInstance.getApi()

    suspend fun getPremieres(currentMonth:String,currentYear:Int): List<PremierMovie> {
        val movies = kinopoiskApi.getPremieres(currentYear, currentMonth)
        delay(1000)
        return movies.items
    }

    suspend fun getPopular(): List<PopularMovie> {
        val movies = kinopoiskApi.getPopular()
        delay(1000)
        return movies.items
    }

    suspend fun getCountryAndGenre(): CountriesGenres {
        val countriesAndGenres = kinopoiskApi.getCountryAndGenres()
        delay(1000)
        return countriesAndGenres
    }

    suspend fun getCompilation(country:Int,genre:Int): List<CompilationsMovie> {
        val movies = kinopoiskApi.getCompilation(country,genre)
        delay(1000)
        return movies.items
    }




}


