package org.sniffsnirr.skillcinema.entities.serialinfo

data class Season(
    val episodes: List<Episode>,
    val number: Int
)