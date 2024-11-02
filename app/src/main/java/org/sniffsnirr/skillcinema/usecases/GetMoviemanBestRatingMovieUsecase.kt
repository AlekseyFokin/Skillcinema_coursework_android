package org.sniffsnirr.skillcinema.usecases

import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.entities.movieman.MoviemanInfo
import org.sniffsnirr.skillcinema.restrepository.KinopoiskRepository
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import java.util.Locale
import javax.inject.Inject

//Usecase - получение по актеру/кинематографисту 10 самых рейтинговых фильмов
@ActivityRetainedScoped
class GetMoviemanBestRatingMovieUsecase @Inject constructor(
    val kinopoiskRepository: KinopoiskRepository,
    val reductionUsecase: ReductionUsecase,
    val decideMovieRVmodelIsViewedOrNotUsecase: DecideMovieRVmodelIsViewedOrNotUsecase
) {

    private suspend fun getBestRatingMovieByMovieman(idStaff: Int): Triple<MoviemanInfo,List<Int>,Int>{
        val filmIdList = mutableListOf<Int>()
        val moviemanInfo = kinopoiskRepository.getMoviemanInfo(idStaff)
         moviemanInfo.films.sortedByDescending { film -> film.ratingDouble }.take(10)
        .map { film -> filmIdList.add(film.filmId) }
        return Triple(moviemanInfo,filmIdList,moviemanInfo.films.size)
    }

    private suspend fun getMovieRVModel(idMovie: Int): MovieRVModel {// получение данных по конкретному фильму и конвертация  к MovieRVModel
        val onlyOneMovie = kinopoiskRepository.getOneMovie(idMovie)
        val movieRvModel= MovieRVModel(
            onlyOneMovie.kinopoiskId,
            onlyOneMovie.posterUrlPreview,
            reductionUsecase.stringReduction(onlyOneMovie.nameRu, 17),
            reductionUsecase.arrayReduction(onlyOneMovie.genres.map { it.genre }, 20, 2),
            "  ${String.format(Locale.US, "%.1f", onlyOneMovie.ratingKinopoisk)}  ",
            false, false, null
        )
        decideMovieRVmodelIsViewedOrNotUsecase.setMovieRVmodelViewed(movieRvModel)
        return movieRvModel
    }

    suspend fun getBestMoviesRVModelByMovieman(idStaff: Int):Triple<MoviemanInfo ,List<MovieRVModel>,Int> {// запрос фильмов по полученному списку id и получение списка MovieRVModel
        val tripleMoviemanInfoListBestMovies = getBestRatingMovieByMovieman(idStaff)
        val bestMoviesRVModelByMovieman = mutableListOf<MovieRVModel>()
        tripleMoviemanInfoListBestMovies.second.map { idMovie -> bestMoviesRVModelByMovieman.add(getMovieRVModel(idMovie)) }
        bestMoviesRVModelByMovieman.add(    //добавляю кнопку
            MovieRVModel(
                isButton = true,
                categoryDescription = Triple("", null, null)
            )
        )
        return Triple(tripleMoviemanInfoListBestMovies.first,bestMoviesRVModelByMovieman,tripleMoviemanInfoListBestMovies.third)
    }
}