package org.sniffsnirr.skillcinema.usecases

import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.entities.onlyonemovie.OnlyOneMovie
import org.sniffsnirr.skillcinema.restrepository.KinopoiskRepository

import javax.inject.Inject
@ActivityRetainedScoped
class GetMovieInfo @Inject constructor(
    val kinopoiskRepository: KinopoiskRepository,
    val reduction: Reduction
){
    suspend fun getInfo(idMovie:Int): OnlyOneMovie {

        return kinopoiskRepository.getOneMovie(idMovie)
        }
}