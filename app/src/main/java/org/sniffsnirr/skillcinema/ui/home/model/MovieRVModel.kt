package org.sniffsnirr.skillcinema.ui.home.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.sniffsnirr.skillcinema.ui.profile.ProfileFragment
import org.sniffsnirr.skillcinema.usecases.GetCountMovieInCollection

// модель данных для rv нижнего уровня, это либо movie либо кнопка
@Parcelize
data class MovieRVModel(
    val kinopoiskId: Int? = null,
    var imageUrl: String = "",
    val movieName: String = "",
    val movieGenre: String = "",
    val rate: String = "",
    var viewed: Boolean = false,
    val isButton: Boolean,
    val categoryDescription: Triple<String, Int?, Int?>? = null//используется если это кнопка
) : Parcelable

