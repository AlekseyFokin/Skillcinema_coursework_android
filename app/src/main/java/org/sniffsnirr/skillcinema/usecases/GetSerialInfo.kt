package org.sniffsnirr.skillcinema.usecases

import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.entities.serialinfo.SeasonsSerial
import org.sniffsnirr.skillcinema.restrepository.KinopoiskRepository
import javax.inject.Inject

//Usecase -  получение информации по сериалу
@ActivityRetainedScoped
class GetSerialInfo @Inject constructor(
    val kinopoiskRepository: KinopoiskRepository
) {

    suspend fun getNumberOfEpisodesOfFirstSeason(idMovie: Int): Int {
        val serialInfo = kinopoiskRepository.getSerialsInfo(idMovie)
        return serialInfo.items[0].episodes.size
    }

    suspend fun getAllSeialInfo(idMovie: Int): SeasonsSerial {
        return kinopoiskRepository.getSerialsInfo(idMovie)

    }
}