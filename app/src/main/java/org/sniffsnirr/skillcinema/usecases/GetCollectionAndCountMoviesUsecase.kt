package org.sniffsnirr.skillcinema.usecases

import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.room.DatabaseRepository
import org.sniffsnirr.skillcinema.room.dbo.CollectionCountMovies
import org.sniffsnirr.skillcinema.ui.profile.ProfileFragment
import javax.inject.Inject

@ActivityRetainedScoped
class GetCollectionAndCountMoviesUsecase @Inject constructor(val databaseRepository: DatabaseRepository) {
    suspend fun getCollectionAndCountMovies(): List<CollectionCountMovies> {
        val listCollection = databaseRepository.getCollectionAndCountMovies().toMutableList()
        val idCollectionNotInRV =
            listOf(ProfileFragment.ID_VIEWED_COLLECTION, ProfileFragment.ID_INTERESTED_COLLECTION)
        val returnList =//убираю из rv коллекции просмотренных  фильмов  и фильмов к которым проявлен интерес
            listCollection.filterNot { collection -> idCollectionNotInRV.contains(collection.id) }
        return returnList
    }
}