package org.sniffsnirr.skillcinema.restrepository

import kotlinx.coroutines.delay
import org.sniffsnirr.skillcinema.entities.Movie
import java.text.SimpleDateFormat
import java.time.Month
import java.util.Date

class KinopoiskRepository {


    suspend fun getPremieres(): List<Movie> {
        val kinopoiskDataSource = KinopoiskDataSource()
        val kinopoiskApi = kinopoiskDataSource.getApi()
        val movies = kinopoiskApi.getPremieres(getCurrentYear(), getCurrentMonth())
        delay(4000)
        return movies.items
    }

    private fun getCurrentMonth(): String {// текущий месяц
        val month = SimpleDateFormat("MM").format(Date())
        return Month.entries[month.toInt() - 1].toString()
    }

    private fun getCurrentYear(): Int {//текущий год
        return SimpleDateFormat("yyyy").format(Date()).toInt()
    }
}


