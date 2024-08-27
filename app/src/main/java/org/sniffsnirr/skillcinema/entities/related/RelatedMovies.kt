package org.sniffsnirr.skillcinema.entities.related

// модель данных для получения списка похожих фильмов
data class RelatedMovies(
    val items: List<RelatedMovie>,
    val total: Int
)