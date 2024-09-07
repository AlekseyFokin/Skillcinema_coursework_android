package org.sniffsnirr.skillcinema.usecases

import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.entities.serialinfo.SeasonsSerial
import org.sniffsnirr.skillcinema.restrepository.KinopoiskRepository
import javax.inject.Inject

@ActivityRetainedScoped
class GetSerialInfo @Inject constructor(
    val kinopoiskRepository: KinopoiskRepository
) {

    suspend fun getNumberOfEpisodsOfFirstSeason(idMovie: Int): Int {
        val serialInfo = kinopoiskRepository.getSerialsInfo(idMovie)
        return serialInfo.items[0].number
    }

    suspend fun getAllSeialInfo(idMovie: Int): SeasonsSerial {
        return kinopoiskRepository.getSerialsInfo(idMovie)

    }
}