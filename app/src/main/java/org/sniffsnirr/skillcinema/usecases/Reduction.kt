package org.sniffsnirr.skillcinema.usecases

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Reduction @Inject constructor() {
    fun arrayReduction(source: List<String>, reductionLength: Int, limitItem: Int): String {
        val string = source.joinToString(
            separator = ", ",
            limit = limitItem
        )
        return if (string.length > reductionLength) {
            string.take(reductionLength).plus("...")
        } else {
            string
        }
    }

    fun stringReduction(string: String?, reductionLength: Int): String {
        return if (string.isNullOrEmpty()) {
            ""
        } else {
            if (string.length > reductionLength) {
                string.take(reductionLength).plus("...")
            } else {
                string
            }
        }
    }
}