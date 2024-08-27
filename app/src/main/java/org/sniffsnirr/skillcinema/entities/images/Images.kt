package org.sniffsnirr.skillcinema.entities.images

// модель данных для получения изображений, связаных с фильмом - список
data class Images(
    val items: List<Image>,
    val total: Int,
    val totalPages: Int
)