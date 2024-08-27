package org.sniffsnirr.skillcinema.entities

// модель жанра используется в характеристиках movie
data class Genre(
    val genre: String
) {
    override fun toString(): String {
        return this.genre
    }
}