package org.sniffsnirr.skillcinema.usecases

import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.entities.movieman.Film
import org.sniffsnirr.skillcinema.entities.movieman.MoviemanInfo
import org.sniffsnirr.skillcinema.restrepository.KinopoiskRepository
import javax.inject.Inject

@ActivityRetainedScoped
class GetMoviemanFilmography @Inject constructor(
    val kinopoiskRepository: KinopoiskRepository,
    val reduction: Reduction
)
{
   suspend fun getMovieByMovieman(idStaff: Int): Pair<Pair<String,String>,Map<String,List<Film>>>{

        val moviemanInfo = kinopoiskRepository.getMoviemanInfo(idStaff)
        val moviemanName=moviemanInfo.nameRu
       val moviemanSex=moviemanInfo.sex
        val mapMovieIdByProfessionKey=moviemanInfo.films.groupBy { film->film.professionKey }

        return Pair(Pair(moviemanName,moviemanSex),mapMovieIdByProfessionKey)
    }
}