package org.sniffsnirr.skillcinema.entities

data class Genre(
    val genre: String
){
    override fun toString(): String {
        return this.genre
    }
}