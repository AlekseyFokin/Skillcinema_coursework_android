package org.sniffsnirr.skillcinema.restrepository

import org.sniffsnirr.skillcinema.entities.compilations.CompilationsMovieList
import org.sniffsnirr.skillcinema.entities.compilations.countriesandgenres.CountriesGenres
import org.sniffsnirr.skillcinema.entities.popular.PopularMovieList
import org.sniffsnirr.skillcinema.entities.premiers.PremierMovieList
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

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
    suspend fun getPopular(// получение популярных фильмов - первая страница (20)
        @Query("type") type: String=TOP_POPULAR_MOVIES,
        @Query("page") page: Int=1
    ): PopularMovieList

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
    ): CompilationsMovieList// подборки


    private companion object {
        private const val api_key = "f1a491f0-8e90-44d1-898a-17656c4ea1de"

        private const val TOP_POPULAR_MOVIES="TOP_POPULAR_MOVIES"
    }

}