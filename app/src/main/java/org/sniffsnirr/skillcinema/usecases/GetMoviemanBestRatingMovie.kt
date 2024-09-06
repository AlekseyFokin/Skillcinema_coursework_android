package org.sniffsnirr.skillcinema.usecases

import android.util.Log
import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.entities.movieman.MoviemanInfo
import org.sniffsnirr.skillcinema.restrepository.KinopoiskRepository
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import java.util.Locale
import javax.inject.Inject

@ActivityRetainedScoped
class GetMoviemanBestRatingMovie @Inject constructor(
    val kinopoiskRepository: KinopoiskRepository,
    val reduction: Reduction
) {

    private suspend fun getBestRatingMovieByMovieman(idStaff: Int): Triple<MoviemanInfo,List<Int>,Int>{
        val filmIdList = mutableListOf<Int>()
        val moviemanInfo = kinopoiskRepository.getMoviemanInfo(idStaff)
         //moviemanInfo.films.onEach { film-> Log.d("BestM","rating___${film.rating}====${film.ratingDouble}")}
        moviemanInfo.films.sortedByDescending { film -> film.ratingDouble }.take(10)
        .map { film -> filmIdList.add(film.filmId) }

        return Triple(moviemanInfo,filmIdList,moviemanInfo.films.size)
    }

    private suspend fun getMovieRVModel(idMovie: Int): MovieRVModel {// получение данных по конкретному фильму и конвертация  к MovieRVModel
        val onlyOneMovie = kinopoiskRepository.getOneMovie(idMovie)
        return MovieRVModel(
            onlyOneMovie.kinopoiskId,
            onlyOneMovie.posterUrlPreview,
            reduction.stringReduction(onlyOneMovie.nameRu, 17),
            reduction.arrayReduction(onlyOneMovie.genres.map { it.genre }, 20, 2),
            "  ${String.format(Locale.US, "%.1f", onlyOneMovie.ratingKinopoisk)}  ",
            false, false, null
        )
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