package org.sniffsnirr.skillcinema.entities

// модель страны используется в характеристиках movie
data class Country(
    val country: String
) {
    override fun toString(): String {
        return country
    }
}