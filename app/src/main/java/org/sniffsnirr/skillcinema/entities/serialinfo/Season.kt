package org.sniffsnirr.skillcinema.entities.serialinfo

// модель данных для получения сезона сериала
data class Season(
    val episodes: List<Episode>,
    val number: Int
)