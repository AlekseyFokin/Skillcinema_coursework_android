package org.sniffsnirr.skillcinema.usecases

import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.sniffsnirr.skillcinema.room.DatabaseRepository
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import org.sniffsnirr.skillcinema.ui.profile.ProfileFragment
import javax.inject.Inject

@ActivityRetainedScoped
class DecideMovieRVmodelIsViewedOrNot @Inject constructor(val databaseRepository: DatabaseRepository) {
    suspend fun setMovieRVmodelViewed(movieRVModel: MovieRVModel) {
        if (movieRVModel.kinopoiskId != null) {
            movieRVModel.viewed = databaseRepository.getCountMovieInCollection(
                movieRVModel.kinopoiskId.toLong(),
                ProfileFragment.ID_VIEWED_COLLECTION,
            ) > 0
        }
    }
}

