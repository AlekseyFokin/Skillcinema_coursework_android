package org.sniffsnirr.skillcinema.ui.home.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

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
) : Parcelable {
    override fun toString(): String {
        return "kinopoiskId=${this.kinopoiskId}, imageUrl=${this.imageUrl}, movieName=${this.movieName}, movieGenre=${this.movieGenre}, rate=${this.rate}, viewed=${this.viewed}, isButton=${this.isButton}, categoryDescription=${this.categoryDescription}"
    }
}

