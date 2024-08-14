package org.sniffsnirr.skillcinema.usecases

import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.entities.compilations.countriesandgenres.Country
import org.sniffsnirr.skillcinema.entities.compilations.countriesandgenres.Genre
import org.sniffsnirr.skillcinema.restrepository.KinopoiskApi
import org.sniffsnirr.skillcinema.restrepository.KinopoiskRepository
import org.sniffsnirr.skillcinema.ui.home.model.MainModel
import javax.inject.Inject

@ActivityRetainedScoped
class HomePageUsecase @Inject constructor(
    val kinopoiskRepository: KinopoiskRepository,
    val getMoviePremiers: GetMoviePremiers,
    val getCollectionMovies: GetCollectionMovies,
    val getDynamicCompilation: GetDynamicCompilation

) {

    suspend fun getCountryAndGenre(): Pair<List<Country>, List<Genre>> {// получение списка стран и жанров
        val countriesAndGenres = kinopoiskRepository.getCountryAndGenre()
        val countries =
            countriesAndGenres.countries.filter { country -> country.country in GREAT_CINEMA_COUNTRIES }// фильтрую фильмы которые произведены в странах из списка
        val genres = countriesAndGenres.genres


        return Pair(countries, genres)
    }

    fun getRandomCountryNGenre(countriesNgenres: Pair<List<Country>, List<Genre>>): Pair<Country, Genre> {//выбор страны и жанра случайным образом
        val mutableCountries = countriesNgenres.first.toMutableList()
        mutableCountries.shuffle()
        val country = mutableCountries[0]

        val mutableGenres = countriesNgenres.second.toMutableList()
        mutableGenres.shuffle()
        val genre = mutableGenres[0]
        return Pair(country, genre)
    }

    suspend fun getHomePageCollections(): List<MainModel> {// получение всех списков для главной страницы
        val bannerModel = MainModel("", emptyList(), Triple("BANNER",null,null),true) // баннер
        val primeres = MainModel(// премьеры
            KinopoiskApi.PREMIERES.second,
            getMoviePremiers.getPremiersForNextTwoWeek(),
            Triple(KinopoiskApi.PREMIERES.first, null, null),
            false)

        val popular = MainModel(
            KinopoiskApi.TOP_POPULAR_MOVIES.second,
            getCollectionMovies.getCollectionMovies(KinopoiskApi.TOP_POPULAR_MOVIES),
            Triple(KinopoiskApi.TOP_POPULAR_MOVIES.first,null,null),
            false
        )

        val countryNgenre = getCountryAndGenre()
        var pairCountryNGenre = getRandomCountryNGenre(countryNgenre)

        val dynamic1 = MainModel(// динамический список 1
            "${pairCountryNGenre.second.genre.replaceFirstChar { it.uppercase() }} ${pairCountryNGenre.first.country}",
            getDynamicCompilation.getCompilation(
                pairCountryNGenre.first.id,
                pairCountryNGenre.second.id
            ),
            Triple(KinopoiskApi.DYNAMIC.first,pairCountryNGenre.first.id,pairCountryNGenre.second.id),
            false
        )

        val top250 = MainModel( // топ - 250
            KinopoiskApi.TOP_250_MOVIES.second,
            getCollectionMovies.getCollectionMovies(KinopoiskApi.TOP_250_MOVIES),
            Triple(KinopoiskApi.TOP_250_MOVIES.first,null,null),
            false
        )
        pairCountryNGenre = getRandomCountryNGenre(countryNgenre)
        val dynamic2 = MainModel(//динамический список 2
            "${pairCountryNGenre.second.genre.replaceFirstChar { it.uppercase() }} ${pairCountryNGenre.first.country}",
            getDynamicCompilation.getCompilation(
                pairCountryNGenre.first.id,
                pairCountryNGenre.second.id
            ),
            Triple(KinopoiskApi.DYNAMIC.first,pairCountryNGenre.first.id, pairCountryNGenre.second.id),
            false
        )

        val populrSerials = MainModel( KinopoiskApi.POPULAR_SERIES.second, // популярные сериалы
            getCollectionMovies.getCollectionMovies(KinopoiskApi.POPULAR_SERIES),
            Triple(KinopoiskApi.POPULAR_SERIES.first,null,null),
            false
        )

            return listOf(bannerModel, primeres, popular, dynamic1, top250, dynamic2, populrSerials)
            // return listOf(bannerModel, primeres, popular,top250)
    }

    private companion object {
        val GREAT_CINEMA_COUNTRIES =
            setOf("США", "Франция", "Австралия", "Испания", "Италия", "Индия")
    }
}