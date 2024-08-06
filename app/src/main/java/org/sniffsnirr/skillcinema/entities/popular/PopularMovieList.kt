package org.sniffsnirr.skillcinema.entities.popular

data class PopularMovieList(
    val items: List<PopularMovie>,
    val total: Int,
    val totalPages: Int
)