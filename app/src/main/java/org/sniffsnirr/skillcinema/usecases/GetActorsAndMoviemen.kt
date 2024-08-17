package org.sniffsnirr.skillcinema.usecases

import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.entities.staff.Staff
import org.sniffsnirr.skillcinema.restrepository.KinopoiskRepository
import javax.inject.Inject

@ActivityRetainedScoped
class GetActorsAndMoviemen @Inject constructor(
    val kinopoiskRepository: KinopoiskRepository
){
   suspend fun getActorsAndMoviemen(movieId:Int):Pair<List<Staff>,List<Staff>>{

      val commonList= kinopoiskRepository.getActorsAndMoviemen(movieId)
      val actorsList=commonList.filter { staff-> staff.professionKey=="ACTOR" }
      val moviemenList=commonList.filter { staff-> staff.professionKey!="ACTOR" }
      return Pair(actorsList,moviemenList)
    }
}