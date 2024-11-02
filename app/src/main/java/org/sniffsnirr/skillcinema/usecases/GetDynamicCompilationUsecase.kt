package org.sniffsnirr.skillcinema.usecases

import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.restrepository.KinopoiskRepository
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import java.util.Locale
import javax.inject.Inject

// получение компиляции на основе комбинации жанра и страны и преобразование полученной модели к MovieRVModel
@ActivityRetainedScoped
class GetDynamicCompilationUsecase @Inject constructor(
    val kinopoiskRepository: KinopoiskRepository,
    val reductionUsecase: ReductionUsecase,
    val decideMovieRVmodelIsViewedOrNotUsecase:DecideMovieRVmodelIsViewedOrNotUsecase
) {
    suspend fun getCompilation(country: Int, genre: Int): List<MovieRVModel> {
        val movieRVModelList = mutableListOf<MovieRVModel>()
        val listCompilationMovies = kinopoiskRepository.getCompilation(country, genre)
        listCompilationMovies.map { movie -> // создаю объекты для отображения в recyclerview
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
                categoryDescription = Triple("DYNAMIC", country, genre)
            )
        )
        return movieRVModelList
    }


}