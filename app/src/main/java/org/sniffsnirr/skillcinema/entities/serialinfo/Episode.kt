package org.sniffsnirr.skillcinema.entities.serialinfo

// модель данных для получения эпизода сериала
data class Episode(
    val episodeNumber: Int,
    val nameEn: String,
    val nameRu: String?,
    val releaseDate: String?,
    val seasonNumber: Int,
    val synopsis: String
)