package org.sniffsnirr.skillcinema.entities.collections

//модель данных для получения готовых коллекций - топ 250 и тд
data class CollectionMovieList(
    val items: List<CollectionMovie>,
    val total: Int,
    val totalPages: Int
)