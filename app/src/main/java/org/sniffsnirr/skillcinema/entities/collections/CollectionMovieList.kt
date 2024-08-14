package org.sniffsnirr.skillcinema.entities.collections

data class CollectionMovieList(
    val items: List<CollectionMovie>,
    val total: Int,
    val totalPages: Int
)