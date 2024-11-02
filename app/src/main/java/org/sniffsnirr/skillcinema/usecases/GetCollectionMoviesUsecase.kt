package org.sniffsnirr.skillcinema.usecases

import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.restrepository.KinopoiskRepository
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import java.util.Locale
import javax.inject.Inject

// получение коллекции фильмов и преобразование полученной модели к модели MovieRVModel
@ActivityRetainedScoped
class GetCollectionMoviesUsecase @Inject constructor(
    val kinopoiskRepository: KinopoiskRepository,
    val reductionUsecase: ReductionUsecase,
    val decideMovieRVmodelIsViewedOrNotUsecase:DecideMovieRVmodelIsViewedOrNotUsecase
) {
    suspend fun getCollectionMovies(collectionType: Pair<String, String>): List<MovieRVModel> {
        val movieRVModelList = mutableListOf<MovieRVModel>()
        val listCollectionMovies = kinopoiskRepository.getCollection(collectionType.first)
        listCollectionMovies.map { movie -> // создаю объекты для отображения в recyclerview
            val movieRVModel = MovieRVModel(
                movie.kinopoiskId,
                movie.posterUrl,
                reductionUsecase.stringReduction(movie.nameRu, 17),
                reductionUsecase.arrayReduction(movie.genres.map { it.genre }, 20, 2),
                "  ${String.format(Locale.US, "%.1f", movie.ratingKinopoisk)}  ",
                viewed = false,
                isButton = false
            )
            decideMovieRVmodelIsViewedOrNotUsecase.setMovieRVmodelViewed(movieRVModel)
            movieRVModelList.add(movieRVModel)
        }
        //добавляю кнопку
        movieRVModelList.add(
            MovieRVModel(
                isButton = true,
                categoryDescription = Triple(collectionType.first, null, null)
            )
        )
        return movieRVModelList
    }
}