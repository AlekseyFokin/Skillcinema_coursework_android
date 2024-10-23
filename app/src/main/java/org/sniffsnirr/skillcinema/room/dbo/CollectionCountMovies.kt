package org.sniffsnirr.skillcinema.room.dbo

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class CollectionCountMovies(
    val id: Long,
    val name: String,
    val embedded: Int,
    val countMovies: Int
)
