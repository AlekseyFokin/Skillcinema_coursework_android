package org.sniffsnirr.skillcinema.usecases

import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.sniffsnirr.skillcinema.room.DatabaseRepository
import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel
import org.sniffsnirr.skillcinema.ui.profile.ProfileFragment
import javax.inject.Inject

//Usecase определения находится ли фильм в списке просмотренных
@ActivityRetainedScoped
class DecideMovieRVmodelIsViewedOrNotUsecase @Inject constructor(val databaseRepository: DatabaseRepository) {
    suspend fun setMovieRVmodelViewed(movieRVModel: MovieRVModel) {
        if (movieRVModel.kinopoiskId != null) {
            movieRVModel.viewed = databaseRepository.getCountMovieInCollection(
                movieRVModel.kinopoiskId.toLong(),
                ProfileFragment.ID_VIEWED_COLLECTION,
            ) > 0
        }
    }
}

