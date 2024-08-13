package org.sniffsnirr.skillcinema.entities.popular

data class CollectionMovieList(
    val items: List<CollectionMovie>,
    val total: Int,
    val totalPages: Int
)