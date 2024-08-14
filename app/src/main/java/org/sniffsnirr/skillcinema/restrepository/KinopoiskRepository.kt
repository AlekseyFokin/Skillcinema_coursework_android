package org.sniffsnirr.skillcinema.restrepository

import kotlinx.coroutines.delay
import org.sniffsnirr.skillcinema.entities.compilations.CompilationsMovie
import org.sniffsnirr.skillcinema.entities.compilations.countriesandgenres.CountriesGenres
import org.sniffsnirr.skillcinema.entities.popular.CollectionMovie
import org.sniffsnirr.skillcinema.entities.premiers.PremierMovie
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KinopoiskRepository @Inject constructor(retrofitInstance: KinopoiskDataSource){
    private val kinopoiskApi = retrofitInstance.getApi()

    suspend fun getPremieres(currentMonth:String,currentYear:Int): List<PremierMovie> {
        val movies = kinopoiskApi.getPremieres(currentYear, currentMonth)
        delay(1000)
        return movies.items
    }

    suspend fun getCollection(collectionType:String,page:Int=1): List<CollectionMovie> {
        val movies = kinopoiskApi.getCollection(collectionType,page)
        delay(1000)
        return movies.items
    }

    suspend fun getCountryAndGenre(): CountriesGenres {
        val countriesAndGenres = kinopoiskApi.getCountryAndGenres()
        delay(1000)
        return countriesAndGenres
    }

    suspend fun getCompilation(country:Int,genre:Int,page:Int=1): List<CompilationsMovie> {
        val movies = kinopoiskApi.getCompilation(country,genre,page)
        delay(1000)
        return movies.items
    }




}


