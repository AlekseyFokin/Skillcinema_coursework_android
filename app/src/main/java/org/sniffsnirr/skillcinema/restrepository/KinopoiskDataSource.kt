package org.sniffsnirr.skillcinema.restrepository

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class KinopoiskDataSource {
    private val interceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    private val retrofit = Retrofit.Builder().baseUrl(BASE_URL).client(client)
        .addConverterFactory(
            GsonConverterFactory.create()
        )
        .build()

    fun getApi(): KinopoiskApi {
        return retrofit.create(KinopoiskApi::class.java)
    }

    companion object {
        const val BASE_URL = "https://kinopoiskapiunofficial.tech"
    }
}