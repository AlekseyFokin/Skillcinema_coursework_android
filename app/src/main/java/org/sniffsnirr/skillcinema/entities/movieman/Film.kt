package org.sniffsnirr.skillcinema.entities.movieman

// модель данных для фильмов, которые можно отсортировать по рейтингу, связаных с кинематографистом
data class Film(
    val description: String,
    val filmId: Int,
    val general: Boolean,
    val nameEn: String,
    val nameRu: String,
    val professionKey: String,
    val rating: String?
): Comparable<Film>{
    val ratingDouble:Double
        get() =rating?.toDoubleOrNull()?:0.0

    override fun compareTo(other: Film):Int {
        val delta= this.ratingDouble.minus((other.ratingDouble))
        return when {
            delta>0.0-> 1
            delta<0.0-> -1
            else -> 0
        }
    }
}
