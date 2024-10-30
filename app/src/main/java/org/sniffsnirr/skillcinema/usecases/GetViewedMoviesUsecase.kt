package org.sniffsnirr.skillcinema.usecases

import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.restrepository.KinopoiskRepository
import org.sniffsnirr.skillcinema.room.DatabaseRepository
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import java.util.Locale
import javax.inject.Inject

@ActivityRetainedScoped
class GetViewedMoviesUsecase @Inject constructor(
    val databaseRepository: DatabaseRepository,
 //   val kinopoiskRepository: KinopoiskRepository,
    val reduction: Reduction,
    val decideMovieRVmodelIsViewedOrNot: DecideMovieRVmodelIsViewedOrNot
) {

    private suspend fun getViewedMoviesDBOFromDb(collectionId: Long) = //получение списока kinopoisk_id из БД
        databaseRepository.getMoviesDboByCollectionIdLimited(collectionId)

//    private suspend fun loadMovieRVModelFromAPI(idMovie: Int): MovieRVModel {// получение данных по конкретному фильму и конвертация  к MovieRVModel
//        val onlyOneMovie = kinopoiskRepository.getOneMovie(idMovie)
//        val movieRVModel=MovieRVModel(
//            onlyOneMovie.kinopoiskId,
//            onlyOneMovie.posterUrlPreview,
//            reduction.stringReduction(onlyOneMovie.nameRu, 17),
//            reduction.arrayReduction(onlyOneMovie.genres.map { it.genre }, 20, 2),
//            "  ${String.format(Locale.US, "%.1f", onlyOneMovie.ratingKinopoisk)}  ",
//            false, false, null
//        )
//        decideMovieRVmodelIsViewedOrNot.setMovieRVmodelViewed(movieRVModel)
//        return movieRVModel
//    }

    suspend fun getViewedMovies(collectionId: Long): List<MovieRVModel> { // получение набора данных для rv
        val viewedMoviesDBO = getViewedMoviesDBOFromDb(collectionId)
        val viewedMoviesRVModel = mutableListOf<MovieRVModel>()
        if (!viewedMoviesDBO.isNullOrEmpty()) {
            viewedMoviesDBO.map {movie ->
                val movieRVModel=MovieRVModel(movie.id_kinopoisk.toInt(),movie.poster,movie.name,movie.genre,movie.rate,false,false,null)
                decideMovieRVmodelIsViewedOrNot.setMovieRVmodelViewed(movieRVModel)
                viewedMoviesRVModel.add( movieRVModel)}
        }
        return viewedMoviesRVModel
    }
}