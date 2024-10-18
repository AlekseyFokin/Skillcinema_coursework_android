package org.sniffsnirr.skillcinema.usecases

import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.entities.movieman.Film
import org.sniffsnirr.skillcinema.restrepository.KinopoiskRepository
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import java.util.Locale
import javax.inject.Inject

@ActivityRetainedScoped
class GetMoviemanFilmography @Inject constructor(
    val kinopoiskRepository: KinopoiskRepository,
    val reduction: Reduction,
    val decideMovieRVmodelIsViewedOrNot: DecideMovieRVmodelIsViewedOrNot
)
{
   suspend fun getMovieByMovieman(idStaff: Int): Pair<Pair<String,String>,Map<String,List<Film>>>{//получение фильмов по мувимену
        val moviemanInfo = kinopoiskRepository.getMoviemanInfo(idStaff)
        val moviemanName=moviemanInfo.nameRu
        val moviemanSex=moviemanInfo.sex
        val mapMovieIdByProfessionKey=moviemanInfo.films.groupBy { film->film.professionKey }// группировка по специальности мувимена
        return Pair(Pair(moviemanName,moviemanSex),mapMovieIdByProfessionKey)
    }

    private suspend fun getMovieRVModel(idMovie: Int): MovieRVModel {// получение данных по конкретному фильму и конвертация  к MovieRVModel
        val onlyOneMovie = kinopoiskRepository.getOneMovie(idMovie)
        val movieRVModel=MovieRVModel(
            onlyOneMovie.kinopoiskId,
            onlyOneMovie.posterUrlPreview,
            reduction.stringReduction(onlyOneMovie.nameRu, 17),
            reduction.arrayReduction(onlyOneMovie.genres.map { it.genre }, 20, 2),
            "  ${String.format(Locale.US, "%.1f", onlyOneMovie.ratingKinopoisk)}  ",
            false, false, null
        )
        decideMovieRVmodelIsViewedOrNot.setMovieRVmodelViewed(movieRVModel)
        return movieRVModel
    }

    suspend fun getMoviesRVModelByMoviemanProfessionKey(filmIdList:List<Int>):List<MovieRVModel> {// запрос фильмов по полученному списку id и получение списка MovieRVModel
        val moviesRVModelByMoviemanProfessionKey = mutableListOf<MovieRVModel>()
        filmIdList.map { idMovie -> moviesRVModelByMoviemanProfessionKey.add(getMovieRVModel(idMovie)) }
        return moviesRVModelByMoviemanProfessionKey
    }
}