package org.sniffsnirr.skillcinema.usecases

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Reduction @Inject constructor(){
    fun arrayReduction(source: List<String>, reductionLength: Int, limitItem: Int): String {
        val string = source.joinToString(
            separator = ", ",
            limit = limitItem
        )
        if (string.length > reductionLength) {
            return string.take(reductionLength).plus("...")
        } else {
            return string
        }
    }
    fun stringReduction(string: String, reductionLength: Int): String {

        if (string.length > reductionLength) {
            return string.take(reductionLength).plus("...")
        } else {
            return string
        }
    }
}