package org.sniffsnirr.skillcinema.usecases

import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.restrepository.KinopoiskRepository
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import javax.inject.Inject
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId
import java.util.Locale

// на основе текущей даты - получение премьер на 2 недели вперед, с учетом конца месяца.
// используется threetenabp - библиотека Backport JSR-310 java8 - для работы со временем
@ActivityRetainedScoped
class GetMoviePremiersUsecase @Inject constructor(
    val kinopoiskRepository: KinopoiskRepository,
    val reductionUsecase: ReductionUsecase,
    val decideMovieRVmodelIsViewedOrNotUsecase: DecideMovieRVmodelIsViewedOrNotUsecase
) {
    private val today = LocalDate.now()
    private val todayPlusTwoWeek = today.plusWeeks(2)


    suspend fun getPremiersForNextTwoWeek(): List<MovieRVModel> {
        val currentMonth = today.month.toString().uppercase(Locale.US)
        val currentYear = today.year
        val plusTwoWeekMonth = todayPlusTwoWeek.month.toString().uppercase(Locale.US)
        val plusTwoWeekYear = todayPlusTwoWeek.year
        val movieRVModelList = mutableListOf<MovieRVModel>()

        val listPrimeres = kinopoiskRepository.getPremieres(currentMonth, currentYear)
        val commonPremierList = listPrimeres.toMutableList()

        if (currentMonth != plusTwoWeekMonth) {// если до конца месяца мение 14 дней - то нужно загружать следующий месяц
            val nextMonthPrimeres =
                kinopoiskRepository.getPremieres(plusTwoWeekMonth, plusTwoWeekYear)
            commonPremierList.addAll(nextMonthPrimeres)
        }
        val filtredPrimeresList =// отфильтровываю фильмы, которые лежат в интервале двух недель
            commonPremierList.filter { movie ->
                (
                        (
                                movie.premiereRu.time >= today.atStartOfDay(ZoneId.systemDefault())
                                    .toInstant().toEpochMilli()
                                ) && (
                                movie.premiereRu.time <= todayPlusTwoWeek.atStartOfDay(ZoneId.systemDefault())
                                    .toInstant().toEpochMilli()
                                )
                        )
            }

        filtredPrimeresList.map { movie -> // создаю объекты для отображения в recyclerview

            val movieRVModel = MovieRVModel(
                movie.kinopoiskId,
                movie.posterUrl,
                reductionUsecase.stringReduction(movie.nameRu, 17),
                reductionUsecase.arrayReduction(movie.genres.map { it.genre }, 20, 2),
                "0",
                false,
                false
            )
            decideMovieRVmodelIsViewedOrNotUsecase.setMovieRVmodelViewed(movieRVModel)
            movieRVModelList.add(movieRVModel)
        }
        //добавляю кнопку
        movieRVModelList.add(
            MovieRVModel(
                isButton = true,
                categoryDescription = Triple("PRIMERES", null, null)
            )
        )
        return movieRVModelList
    }
}

