package org.sniffsnirr.skillcinema.ui.search

import androidx.paging.PagingSource
import androidx.paging.PagingState
import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.entities.compilations.CompilationsMovie
import org.sniffsnirr.skillcinema.restrepository.KinopoiskRepository
import org.sniffsnirr.skillcinema.ui.collections.paging.compilations.MoviePagingSource
import org.sniffsnirr.skillcinema.ui.collections.paging.compilations.MoviePagingSource.Companion
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import org.sniffsnirr.skillcinema.usecases.DecideMovieRVmodelIsViewedOrNotUsecase
import org.sniffsnirr.skillcinema.usecases.ReductionUsecase
import java.util.Locale

// класс загрузки пагинирующей коллекции фильмов на основе фильтра с приведением к MovieRVModel типу
@ActivityRetainedScoped
class SearchResultPagingSource(
    val kinopoiskRepository: KinopoiskRepository,
    val reductionUsecase: ReductionUsecase,
    val queryParams: QueryParams,
    val decideMovieRVmodelIsViewedOrNotUsecase: DecideMovieRVmodelIsViewedOrNotUsecase
) : PagingSource<Int, MovieRVModel>() {

    override fun getRefreshKey(state: PagingState<Int, MovieRVModel>): Int = FIRST_PAGE

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieRVModel> {
        val page = params.key ?: SearchResultPagingSource.FIRST_PAGE
        return kotlin.runCatching {
            kinopoiskRepository.getSearchResult(
                queryParams.country,
                queryParams.genre,
                queryParams.order,
                queryParams.type,
                queryParams.ratingFrom,
                queryParams.ratingTo,
                queryParams.yearFrom,
                queryParams.yearTo,
                queryParams.keyword,
                page
            )
        }.fold(
            onSuccess = {
                LoadResult.Page(
                    data = if (queryParams.onlyUnviewed) castToMovieRVModel(it).filter { movie -> movie.viewed == false }
                    else castToMovieRVModel(it),
                    prevKey = null,
                    nextKey = if (it.isEmpty()) null else page + 1
                )
            },
            onFailure = { LoadResult.Error(it) }
        )
    }

    private suspend fun castToMovieRVModel(movies: List<CompilationsMovie>): List<MovieRVModel> {// функция приведения CompilationsMovie к MovieRVModel
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