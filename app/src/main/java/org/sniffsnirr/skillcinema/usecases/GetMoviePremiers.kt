package org.sniffsnirr.skillcinema.usecases

import android.util.Log
import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.entities.Movie
import org.sniffsnirr.skillcinema.restrepository.KinopoiskRepository
import org.sniffsnirr.skillcinema.ui.home.model.MainModel
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import java.text.SimpleDateFormat
import javax.inject.Inject
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset
import java.util.Locale


@ActivityRetainedScoped
class GetMoviePremiers @Inject constructor(val kinopoiskRepository: KinopoiskRepository) {
    val today = LocalDate.now()
    val todayPlusTwoWeek = today.plusWeeks(2)

        //   suspend fun getPremiersForNextTwoWeek(): List<Movie> {
        suspend fun getPremiersForNextTwoWeek(): List<MainModel> {
        val currentMonth = today.month.toString().uppercase(Locale.US)
        val currentYear = today.year.toInt()
        val plusTwoWeekMonth = todayPlusTwoWeek.month.toString().uppercase(Locale.US)
        val plusTwoWeekYear = todayPlusTwoWeek.year.toInt()

        val listPrimeres = kinopoiskRepository.getPremieres(currentMonth, currentYear)
        var commonPremierList=listPrimeres.toMutableList()

        if (currentMonth != plusTwoWeekMonth) {
            val nextMonthPrimeres = kinopoiskRepository.getPremieres(plusTwoWeekMonth, plusTwoWeekYear)
                commonPremierList.addAll(nextMonthPrimeres)
        }
            val filtredPrimeresList =
                commonPremierList.filter { movie ->
                    (
                            (
                                    movie.premiereRu.time >= today.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                            ) && (
                                    movie.premiereRu.time <= todayPlusTwoWeek.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                                    )
                            )
                }

           val movieRVModelList= mutableListOf<MovieRVModel>()

            filtredPrimeresList.map{movie-> val movieRVModel=MovieRVModel(movie.posterUrl,
                movie.nameRu.take(17),movie.genres.joinToString(
                separator = ", ",
                limit = 2

            ).take(20),"0",false)
                movieRVModelList.add(movieRVModel)
            }

            val bannerModel=MainModel("", emptyList(),true)
            val mainModel=MainModel("Премьеры",movieRVModelList,false)
            val mainModelList= listOf<MainModel>(bannerModel,mainModel)

            return mainModelList
        }
    }

//    private fun getCurrentMonth(): String {// текущий месяц
//        val month = SimpleDateFormat("MM").format(Date())
//        return Month.entries[month.toInt() - 1].toString()
//    }
//
//    private fun getCurrentYear(): Int {//текущий год
//        return SimpleDateFormat("yyyy").format(Date()).toInt()
//    }
