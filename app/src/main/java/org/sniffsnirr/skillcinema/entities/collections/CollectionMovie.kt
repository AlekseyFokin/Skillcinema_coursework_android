package org.sniffsnirr.skillcinema.entities.collections

import org.sniffsnirr.skillcinema.entities.Country
import org.sniffsnirr.skillcinema.entities.Genre

//модель данных для получения готовых коллекций - топ 250 и тд - отдельное кино
data class CollectionMovie(
    val countries: List<Country>,
    val coverUrl: String,
    val description: String,
    val genres: List<Genre>,
    val imdbId: String,
    val kinopoiskId: Int,
    val logoUrl: String,
    val nameEn: Any,
    val nameOriginal: String,
    val nameRu: String,
    val posterUrl: String,
    val posterUrlPreview: String,
    val ratingAgeLimits: String,
    val ratingImdb: Double,
    val ratingKinopoisk: Double,
    val type: String,
    val year: Int
)