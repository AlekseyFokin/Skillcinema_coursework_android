package org.sniffsnirr.skillcinema.entities

data class MovieList(
    val items: List<Movie>,
    val total: Int
)