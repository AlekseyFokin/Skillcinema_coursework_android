package org.sniffsnirr.skillcinema.entities

import java.util.Date

data class Movie(
    val countries: List<Country>,
    val duration: Int,
    val genres: List<Genre>,
    val kinopoiskId: Int,
    val nameEn: String,
    val nameRu: String,
    val posterUrl: String,
    val posterUrlPreview: String,
    val premiereRu: Date,
    val year: Int
)