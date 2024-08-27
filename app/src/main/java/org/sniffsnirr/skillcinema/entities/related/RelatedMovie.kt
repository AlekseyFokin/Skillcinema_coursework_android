package org.sniffsnirr.skillcinema.entities.related

// модель данных для получения похожего фильма

data class RelatedMovie(
    val filmId: Int,
    val nameEn: String,
    val nameOriginal: String,
    val nameRu: String,
    val posterUrl: String,
    val posterUrlPreview: String,
    val relationType: String
)