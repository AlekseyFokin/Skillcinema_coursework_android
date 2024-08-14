package org.sniffsnirr.skillcinema.ui.collections.paging.compilations

import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import androidx.paging.PagingSource
import androidx.paging.PagingState
import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.entities.compilations.CompilationsMovie
import org.sniffsnirr.skillcinema.restrepository.KinopoiskRepository
import org.sniffsnirr.skillcinema.usecases.Reduction
import java.util.Locale

@ActivityRetainedScoped
class MoviePagingSource constructor(val kinopoiskRepository: KinopoiskRepository,val reduction: Reduction, val collectionDescription:Triple<String,Int,Int>) :
    PagingSource<Int, MovieRVModel>() {
    override fun getRefreshKey(state: PagingState<Int, MovieRVModel>): Int = FIRST_PAGE

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieRVModel> {
        val page = params.key ?: FIRST_PAGE
        return kotlin.runCatching {
            kinopoiskRepository.getCompilation(collectionDescription.second,collectionDescription.third, page)
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

    fun castToMovieRVModel(movies:List<CompilationsMovie>):List<MovieRVModel>
    {
        val movieRVModelList = mutableListOf<MovieRVModel>()
        movies.map { movie -> // создаю объекты для отображения в recyclerview
            val movieRVModel = MovieRVModel(
                movie.kinopoiskId,
                movie.posterUrl,
                reduction.stringReduction(movie.nameRu, 17),
                reduction.arrayReduction(movie.genres.map { it.genre }, 20, 2),
                "  ${String.format(Locale.US, "%.1f", movie.ratingKinopoisk)}  ",
                false,
                false
            )
            movieRVModelList.add(movieRVModel)
        }
        return movieRVModelList
    }

    companion object
    const

    val FIRST_PAGE = 1
}