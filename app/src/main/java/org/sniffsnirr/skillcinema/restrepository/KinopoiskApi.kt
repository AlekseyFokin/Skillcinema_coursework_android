package org.sniffsnirr.skillcinema.restrepository

import org.sniffsnirr.skillcinema.entities.compilations.CompilationsMovieList
import org.sniffsnirr.skillcinema.entities.compilations.countriesandgenres.CountriesGenres
import org.sniffsnirr.skillcinema.entities.collections.CollectionMovieList
import org.sniffsnirr.skillcinema.entities.images.Images
import org.sniffsnirr.skillcinema.entities.movieman.MoviemanInfo
import org.sniffsnirr.skillcinema.entities.onlyonemovie.OnlyOneMovie
import org.sniffsnirr.skillcinema.entities.premiers.PremierMovieList
import org.sniffsnirr.skillcinema.entities.related.RelatedMovies
import org.sniffsnirr.skillcinema.entities.serialinfo.SeasonsSerial
import org.sniffsnirr.skillcinema.entities.staff.Staff
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
    suspend fun getCountryAndGenres(): CountriesGenres// получение стран и жанров

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

    @Headers(
        "X-API-KEY: $api_key",
        "Content-type: application/json"
    )
    @GET("/api/v2.2/films/{movie}")
    suspend fun getOnlyOneMovie( // информация по одному фильму
        @Path("movie") idMovie: Int
    ): OnlyOneMovie

    @Headers(
        "X-API-KEY: $api_key",
        "Content-type: application/json"
    )
    @GET("/api/v1/staff")
    suspend fun getActorsAndMoviemen( // получение всех актеров и кинематографистов по одному фильму
        @Query("filmId") filmId: Int
    ): List<Staff>

    @Headers(
        "X-API-KEY: $api_key",
        "Content-type: application/json"
    )
    @GET("/api/v2.2/films/{movie}/images")
    suspend fun getImageByType(
        @Path("movie") idMovie: Int,
        @Query("type") type: String,
        @Query("page") page: Int
    ): Images// получение изображений по конкретному фильму и типу изображений - с пагинацией

    @Headers(
        "X-API-KEY: $api_key",
        "Content-type: application/json"
    )
    @GET("/api/v2.2/films/{movie}/similars")
    suspend fun getRelatedMovies(@Path("movie") idMovie: Int): RelatedMovies // получение подобных фильмов

    @Headers(
        "X-API-KEY: $api_key",
        "Content-type: application/json"
    )
    @GET("/api/v1/staff/{staffId}")
    suspend fun getMoviemanInfo(@Path("staffId") staffId: Int): MoviemanInfo // получение подобных фильмов

    @Headers(
        "X-API-KEY: $api_key",
        "Content-type: application/json"
    )
    @GET("/api/v2.2/films/{movie}/seasons")
    suspend fun getSeasonsSerialInfo(@Path("movie") idMovie: Int): SeasonsSerial // получение информации по эпизодам и сериям сериала


    companion object {
        private const val api_key ="f1a491f0-8e90-44d1-898a-17656c4ea1de"

        //"d0e3296d-48db-4922-be82-04f28e857cec"
        //"f7f09f94-0bce-420d-8b60-bfea35d9fff6"
        // "697ee5de-bb24-4cb5-988d-2d8ead05f857"
        //"f1a491f0-8e90-44d1-898a-17656c4ea1de"
        //"bfba821e-cc5e-4637-881a-4be971e3968f"

        val TOP_POPULAR_MOVIES = Pair("TOP_POPULAR_MOVIES", "Популярное")
        val TOP_250_MOVIES = Pair("TOP_250_MOVIES", "Топ-250")
        val POPULAR_SERIES = Pair("POPULAR_SERIES", "Популярные сериалы")
        val PREMIERES = Pair("PREMIERES", "Премьеры")
        val DYNAMIC = Pair("DYNAMIC", "Основаны на фильтах")
    }

}