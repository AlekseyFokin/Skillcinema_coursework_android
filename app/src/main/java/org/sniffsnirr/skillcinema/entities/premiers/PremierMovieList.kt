package org.sniffsnirr.skillcinema.entities.premiers

// модель данных для получения списка премьер
data class PremierMovieList(
    val items: List<PremierMovie>,
    val total: Int
)