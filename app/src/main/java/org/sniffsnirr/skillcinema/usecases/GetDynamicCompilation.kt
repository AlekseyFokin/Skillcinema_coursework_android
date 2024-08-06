package org.sniffsnirr.skillcinema.usecases

import org.sniffsnirr.skillcinema.entities.compilations.countriesandgenres.Country
import org.sniffsnirr.skillcinema.entities.compilations.countriesandgenres.Genre
import org.sniffsnirr.skillcinema.restrepository.KinopoiskRepository
import org.sniffsnirr.skillcinema.ui.home.model.MainModel
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import java.util.Locale
import javax.inject.Inject

class GetDynamicCompilation @Inject constructor(
    val kinopoiskRepository: KinopoiskRepository,val reduction: Reduction
){
    val movieRVModelList = mutableListOf<MovieRVModel>()
    //private var countries=listOf<Country>()
    //private var genres=listOf<Genre>()

   suspend fun getCountriesAndGenres():Pair<List<Country>,List<Genre>>{// получение списка стран и жанров
       val countriesAndGenres = kinopoiskRepository.getCountryAndGenre()
       val countries=countriesAndGenres.countries.filter { country-> country.country in GREAT_CINEMA_COUNTRIES}// фильтрую фильмы которые произведены в странах из списка
       val genres=countriesAndGenres.genres
       return Pair(countries,genres)
    }

   suspend fun getCompilation(): List<MainModel>{
       var countriesNgenres=getCountriesAndGenres()
       val countries=countriesNgenres.first// получение случайной страны
       val mutableCountries=countries.toMutableList()
       mutableCountries.shuffle()
       val country=mutableCountries[0]

       val genres=countriesNgenres.second// получение случайного жанра
       val mutableGenres=genres.toMutableList()
       mutableGenres.shuffle()
       val genre=mutableGenres[0]

       val listCompilationMovies = kinopoiskRepository.getCompilation(country.id,genre.id)
       listCompilationMovies.map { movie -> // создаю объекты для отображения в recyclerview
           val movieRVModel = MovieRVModel(
               movie.posterUrl,
               reduction.stringReduction(movie.nameRu, 17),
               reduction.arrayReduction(movie.genres.map { it.genre }, 20, 2),
               "  ${String.format(Locale.US, "%.1f", movie.ratingKinopoisk)}  ",
               false
           )
           movieRVModelList.add(movieRVModel)
       }
       val bannerModel = MainModel("", emptyList(), true)
       val mainModel = MainModel("${genre.genre.replaceFirstChar { it.uppercase() }} ${country.country}", movieRVModelList, false)
       val mainModelList = listOf(bannerModel, mainModel)
       return mainModelList
   }

    companion object{
       val GREAT_CINEMA_COUNTRIES=setOf("США","Франция","Австралия","Испания","Италия","Индия")
    }

}