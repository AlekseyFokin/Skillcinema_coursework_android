package org.sniffsnirr.skillcinema.entities.premiers

data class PremierMovieList(
    val items: List<PremierMovie>,
    val total: Int
)