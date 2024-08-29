package org.sniffsnirr.skillcinema.restrepository

import kotlinx.coroutines.delay
import org.sniffsnirr.skillcinema.entities.compilations.CompilationsMovie
import org.sniffsnirr.skillcinema.entities.compilations.countriesandgenres.CountriesGenres
import org.sniffsnirr.skillcinema.entities.collections.CollectionMovie
import org.sniffsnirr.skillcinema.entities.images.Image
import org.sniffsnirr.skillcinema.entities.images.Images
import org.sniffsnirr.skillcinema.entities.movieman.MoviemanInfo
import org.sniffsnirr.skillcinema.entities.onlyonemovie.OnlyOneMovie
import org.sniffsnirr.skillcinema.entities.premiers.PremierMovie
import org.sniffsnirr.skillcinema.entities.related.RelatedMovies
import org.sniffsnirr.skillcinema.entities.staff.Staff
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KinopoiskRepository @Inject constructor(retrofitInstance: KinopoiskDataSource) {
    private val kinopoiskApi = retrofitInstance.getApi()

    suspend fun getPremieres(
        currentMonth: String,
        currentYear: Int
    ): List<PremierMovie> {// получение премьер
        val movies = kinopoiskApi.getPremieres(currentYear, currentMonth)
        return movies.items
    }

    suspend fun getCollection(
        collectionType: String,
        page: Int = 1
    ): List<CollectionMovie> {// получение готовых коллекций фильмов типа топ - 250
        val movies = kinopoiskApi.getCollection(collectionType, page)
        return movies.items
    }

    suspend fun getCountryAndGenre(): CountriesGenres {// получение списка жанров и стран
        val countriesAndGenres = kinopoiskApi.getCountryAndGenres()
        return countriesAndGenres
    }

    suspend fun getCompilation(
        country: Int,
        genre: Int,
        page: Int = 1
    ): List<CompilationsMovie> {// получение компиляций - списков фильмов на основе выбора страны и жанра
        val movies = kinopoiskApi.getCompilation(country, genre, page)
        return movies.items
    }

    suspend fun getOneMovie(idMovie: Int): OnlyOneMovie { // получение данных по одному фильму - по id
        return kinopoiskApi.getOnlyOneMovie(idMovie)
    }

    suspend fun getActorsAndMoviemen(movieId: Int): List<Staff> {// получение всех актеров и кинематографистов , связанных с конкретным фильмом
        return kinopoiskApi.getActorsAndMoviemen(movieId)
    }

    suspend fun getImages(
        idMovie: Int,
        type: String,
        page: Int = 1
    ): List<Image> {// получение изображений определенного типа, связанных с конкретным фильмом
        val images = kinopoiskApi.getImageByType(idMovie, type, page)
        return images.items
    }

    suspend fun getRelatedMovies(idMovie: Int): RelatedMovies {// получение списка похожих фильмов
        return kinopoiskApi.getRelatedMovies(idMovie)
    }

    suspend fun getMoviemanInfo(idStaff:Int):MoviemanInfo{
        return kinopoiskApi.getMoviemanInfo(idStaff)
    }

}


