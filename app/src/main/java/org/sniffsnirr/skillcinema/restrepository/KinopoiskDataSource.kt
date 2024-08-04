package org.sniffsnirr.skillcinema.restrepository

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class KinopoiskDataSource @Inject constructor(){
    private val interceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    var gson: Gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd")
        .create()
    private val retrofit = Retrofit.Builder().baseUrl(BASE_URL).client(client)
        .addConverterFactory(
            GsonConverterFactory.create(gson)
        )
        .build()

    fun getApi(): KinopoiskApi {
        return retrofit.create(KinopoiskApi::class.java)
    }

    companion object {
        const val BASE_URL = "https://kinopoiskapiunofficial.tech"
    }
}