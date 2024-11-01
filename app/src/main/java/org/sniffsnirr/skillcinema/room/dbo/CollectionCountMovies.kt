package org.sniffsnirr.skillcinema.room.dbo

// модель данных к запросу коллекции и количества фильмов в ней
data class CollectionCountMovies(
    val id: Long,
    val name: String,
    val embedded: Int,
    val countMovies: Int
)
