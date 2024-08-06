package org.sniffsnirr.skillcinema.entities.compilations

data class CompilationsMovieList(
    val items: List<CompilationsMovie>,
    val total: Int,
    val totalPages: Int
)