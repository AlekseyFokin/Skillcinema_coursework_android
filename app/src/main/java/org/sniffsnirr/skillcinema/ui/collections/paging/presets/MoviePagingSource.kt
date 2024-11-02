package org.sniffsnirr.skillcinema.ui.collections.paging.presets

import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import androidx.paging.PagingSource
import androidx.paging.PagingState
import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.entities.collections.CollectionMovie
import org.sniffsnirr.skillcinema.restrepository.KinopoiskRepository
import org.sniffsnirr.skillcinema.usecases.DecideMovieRVmodelIsViewedOrNotUsecase
import org.sniffsnirr.skillcinema.usecases.ReductionUsecase
import java.util.Locale

// класс загрузки пагинирующей готовой коллекции с приведением к MovieRVModel типу
@ActivityRetainedScoped
class MoviePagingSource(
    val kinopoiskRepository: KinopoiskRepository,
    val reductionUsecase: ReductionUsecase,
    private val collectionType: String,
    val decideMovieRVmodelIsViewedOrNotUsecase: DecideMovieRVmodelIsViewedOrNotUsecase
) :
    PagingSource<Int, MovieRVModel>() {
    override fun getRefreshKey(state: PagingState<Int, MovieRVModel>): Int = FIRST_PAGE

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieRVModel> {
        val page = params.key ?: FIRST_PAGE
        return kotlin.runCatching {
            kinopoiskRepository.getCollection(collectionType, page)
        }.fold(
            onSuccess = {
                LoadResult.Page(
                    data = castToMovieRVModel(it),
                    prevKey = null,
                    nextKey = if (it.isEmpty()) null else page + 1
                )
            },
            onFailure = { LoadResult.Error(it) }
        )
    }

   private suspend fun castToMovieRVModel(movies: List<CollectionMovie>): List<MovieRVModel> { // приведение CollectionMovie к MovieRVModel
        val movieRVModelList = mutableListOf<MovieRVModel>()
        movies.map { movie -> // создаю объекты для отображения в recyclerview
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
        return movieRVModelList
    }

    companion object {
        const val FIRST_PAGE = 1
    }

}