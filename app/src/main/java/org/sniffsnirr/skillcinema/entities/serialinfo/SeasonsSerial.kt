package org.sniffsnirr.skillcinema.entities.serialinfo

// модель данных для получения списка сезонов сериала
data class SeasonsSerial(
    val items: List<Season>,
    val total: Int
)