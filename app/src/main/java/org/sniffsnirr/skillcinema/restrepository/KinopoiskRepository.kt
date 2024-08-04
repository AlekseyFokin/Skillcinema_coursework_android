package org.sniffsnirr.skillcinema.restrepository

import kotlinx.coroutines.delay
import org.sniffsnirr.skillcinema.entities.Movie
import java.text.SimpleDateFormat
import java.time.Month
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KinopoiskRepository @Inject constructor(val retrofitInstance: KinopoiskDataSource){

    suspend fun getPremieres(currentMonth:String,currentYear:Int): List<Movie> {
        //val kinopoiskDataSource = KinopoiskDataSource()
        val kinopoiskApi = retrofitInstance.getApi()
        val movies = kinopoiskApi.getPremieres(currentYear, currentMonth)
        delay(4000)
        return movies.items
    }


}


