package org.sniffsnirr.skillcinema.usecases

import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.entities.onlyonemovie.OnlyOneMovie
import org.sniffsnirr.skillcinema.restrepository.KinopoiskRepository

import javax.inject.Inject

// получение информации по фильму
@ActivityRetainedScoped
class GetMovieInfoUsecase @Inject constructor(
    val kinopoiskRepository: KinopoiskRepository
) {
    suspend fun getInfo(idMovie: Int): OnlyOneMovie {
        return kinopoiskRepository.getOneMovie(idMovie)
    }
}