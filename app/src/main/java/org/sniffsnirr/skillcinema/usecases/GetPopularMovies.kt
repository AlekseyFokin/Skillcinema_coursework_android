package org.sniffsnirr.skillcinema.usecases

import org.sniffsnirr.skillcinema.restrepository.KinopoiskRepository
import org.sniffsnirr.skillcinema.ui.home.model.MainModel
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import java.util.Locale
import javax.inject.Inject

class GetPopularMovies @Inject constructor(
    val kinopoiskRepository: KinopoiskRepository,
    val reduction: Reduction
    ) {
    val movieRVModelList = mutableListOf<MovieRVModel>()

    suspend fun getPopularMovies(): List<MainModel> {
        val listPopularMovies = kinopoiskRepository.getPopular()
        listPopularMovies.map { movie -> // создаю объекты для отображения в recyclerview
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
        val mainModel = MainModel("Популярное", movieRVModelList, false)
        val mainModelList = listOf(bannerModel, mainModel)
        return mainModelList
    }
}