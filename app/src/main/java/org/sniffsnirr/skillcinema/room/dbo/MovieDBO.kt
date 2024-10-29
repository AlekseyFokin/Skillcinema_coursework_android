package org.sniffsnirr.skillcinema.room.dbo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "movie",
    indices = [Index("id_kinopoisk"), Index("id_set")],
    foreignKeys = [
        ForeignKey(
            onDelete = CASCADE,
            entity = CollectionDBO::class,
            parentColumns = ["id"],
            childColumns = ["id_set"]
        )
    ]
)
class MovieDBO(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "id_set") val id_set: Long,
    @ColumnInfo(name = "id_kinopoisk") val id_kinopoisk: Long,
    @ColumnInfo(name = "poster") val poster: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "genre") val genre: String,
    @ColumnInfo(name = "rate") val rate: String,

)

