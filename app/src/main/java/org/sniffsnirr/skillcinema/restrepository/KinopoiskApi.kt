package org.sniffsnirr.skillcinema.restrepository

import org.sniffsnirr.skillcinema.entities.compilations.CompilationsMovieList
import org.sniffsnirr.skillcinema.entities.compilations.countriesandgenres.CountriesGenres
import org.sniffsnirr.skillcinema.entities.collections.CollectionMovieList
import org.sniffsnirr.skillcinema.entities.onlyonemovie.OnlyOneMovie
import org.sniffsnirr.skillcinema.entities.premiers.PremierMovieList
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import retrofit2.http.Path

interface KinopoiskApi {
    @Headers(
        "X-API-KEY: $api_key",
        "Content-type: application/json"
    )
    @GET("/api/v2.2/films/premieres")
    suspend fun getPremieres(// получение всех премьер
        @Query("year") year: Int,
        @Query("month") month: String
    ): PremierMovieList

    @Headers(
        "X-API-KEY: $api_key",
        "Content-type: application/json"
    )
    @GET("/api/v2.2/films/collections")
    suspend fun getCollection(// получение популярных фильмов и тд - первая страница (20) с пагинацией
        @Query("type") type: String,
        @Query("page") page: Int
    ): CollectionMovieList

    @Headers(
        "X-API-KEY: $api_key",
        "Content-type: application/json"
    )
    @GET("/api/v2.2/films/filters")
    suspend fun getCountryAndGenres(): CountriesGenres// получение стран и жанр

    @Headers(
        "X-API-KEY: $api_key",
        "Content-type: application/json"
    )
    @GET("/api/v2.2/films")
    suspend fun getCompilation(
        @Query("countries") countries: Int,
        @Query("genres") genres: Int,
        @Query("page") page: Int,
    ): CompilationsMovieList// подборки - компиляции с выбором страны и жанра с пагинацией

    @GET("/api/v2.2/films/{movie}")
    suspend fun getOnlyOneMovie(
        @Path("movie") idMovie: Int): OnlyOneMovie// подборки - компиляции с выбором страны и жанра с пагинацией


    companion object {
        private const val api_key = "f1a491f0-8e90-44d1-898a-17656c4ea1de"

        val TOP_POPULAR_MOVIES = Pair("TOP_POPULAR_MOVIES", "Популярное")
        val TOP_250_MOVIES = Pair("TOP_250_MOVIES", "Топ-250")
        val POPULAR_SERIES = Pair("POPULAR_SERIES", "Популярные сериалы")
        val PREMIERES = Pair("PREMIERES", "Премьеры")
        val DYNAMIC = Pair("DYNAMIC", "Основаны на фильтах")
    }

}