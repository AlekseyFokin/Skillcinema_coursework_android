package org.sniffsnirr.skillcinema.usecases

import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.restrepository.KinopoiskRepository
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import java.util.Locale
import javax.inject.Inject
@ActivityRetainedScoped
class GetCollectionMovies @Inject constructor(
    val kinopoiskRepository: KinopoiskRepository,
    val reduction: Reduction
    ) {
    suspend fun getCollectionMovies(collectionType:Pair<String,String>): List<MovieRVModel> {
        val movieRVModelList = mutableListOf<MovieRVModel>()
        val listCollectionMovies = kinopoiskRepository.getCollection(collectionType.first)
        listCollectionMovies.map { movie -> // создаю объекты для отображения в recyclerview
            val movieRVModel = MovieRVModel(
                movie.kinopoiskId,
                movie.posterUrl,
                reduction.stringReduction(movie.nameRu, 17),
                reduction.arrayReduction(movie.genres.map { it.genre }, 20, 2),
                "  ${String.format(Locale.US, "%.1f", movie.ratingKinopoisk)}  ",
                viewed = false,
                isButton = false
            )
            movieRVModelList.add(movieRVModel)
        }
        //добавляю кнопку
        movieRVModelList.add(MovieRVModel(isButton = true, categoryDescription = Triple(collectionType.first,null,null)))
        return movieRVModelList
    }
}