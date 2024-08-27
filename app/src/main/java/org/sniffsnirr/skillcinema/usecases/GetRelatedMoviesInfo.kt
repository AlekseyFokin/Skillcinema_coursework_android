package org.sniffsnirr.skillcinema.usecases

import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.restrepository.KinopoiskRepository
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import java.util.Locale
import javax.inject.Inject

@ActivityRetainedScoped
class GetRelatedMoviesInfo @Inject constructor(
    val kinopoiskRepository: KinopoiskRepository,
    val reduction: Reduction
) {
    private suspend fun getRelatedMoviesIdList(idMovie: Int): List<Int> {// получений списка id похожих фильмов
        val idMovieList = mutableListOf<Int>()
        val relatedMovies = kinopoiskRepository.getRelatedMovies(idMovie)
        relatedMovies.items.map { movie -> idMovieList.add(movie.filmId) }
        return idMovieList
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

    suspend fun getRelatedMoviesRVModel(idMovie: Int): List<MovieRVModel> {// запрос фильмов по полученному списку id и получение списка MovieRVModel
        val listRelatedMovies = getRelatedMoviesIdList(idMovie)
        val relatedMoviesRVModel = mutableListOf<MovieRVModel>()
        listRelatedMovies.map { idMovie -> relatedMoviesRVModel.add(getMovieRVModel(idMovie)) }
        return relatedMoviesRVModel
    }
}