package org.sniffsnirr.skillcinema.restrepository

import org.sniffsnirr.skillcinema.entities.MovieList
import retrofit2.Response
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
    ): MovieList


    private companion object {
        private const val api_key = "f1a491f0-8e90-44d1-898a-17656c4ea1de"
    }

}