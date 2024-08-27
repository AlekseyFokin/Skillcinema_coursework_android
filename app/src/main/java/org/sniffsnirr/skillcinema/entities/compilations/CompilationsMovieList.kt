package org.sniffsnirr.skillcinema.entities.compilations

//модель данных для получения коллекций на основе выбора стран и  жанров
data class CompilationsMovieList(
    val items: List<CompilationsMovie>,
    val total: Int,
    val totalPages: Int
)