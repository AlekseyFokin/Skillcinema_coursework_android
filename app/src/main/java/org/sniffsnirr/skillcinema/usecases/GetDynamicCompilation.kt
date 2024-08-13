package org.sniffsnirr.skillcinema.usecases

import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.entities.compilations.countriesandgenres.Country
import org.sniffsnirr.skillcinema.entities.compilations.countriesandgenres.Genre
import org.sniffsnirr.skillcinema.restrepository.KinopoiskRepository
import org.sniffsnirr.skillcinema.ui.home.model.MainModel
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import java.util.Locale
import javax.inject.Inject
@ActivityRetainedScoped
class GetDynamicCompilation @Inject constructor(
    val kinopoiskRepository: KinopoiskRepository,val reduction: Reduction
){
   suspend fun getCompilation(country:Int,genre:Int): List<MovieRVModel>{
       val movieRVModelList = mutableListOf<MovieRVModel>()
       val listCompilationMovies = kinopoiskRepository.getCompilation(country,genre)
       listCompilationMovies.map { movie -> // создаю объекты для отображения в recyclerview
           val movieRVModel = MovieRVModel(
               movie.kinopoiskId,
               movie.posterUrl,
               reduction.stringReduction(movie.nameRu, 17),
               reduction.arrayReduction(movie.genres.map { it.genre }, 20, 2),
               "  ${String.format(Locale.US, "%.1f", movie.ratingKinopoisk)}  ",
               false,
               false
           )
           movieRVModelList.add(movieRVModel)
       }
       //добавляю кнопку
       movieRVModelList.add(MovieRVModel(isButton = true,categoryDescription = Triple("DYNAMIC",country,genre)))
       return movieRVModelList
   }



}