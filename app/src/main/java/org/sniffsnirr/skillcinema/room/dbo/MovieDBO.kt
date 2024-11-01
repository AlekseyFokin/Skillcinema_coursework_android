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
    @ColumnInfo(name = "id_set") val id_set: Long,// связь с таблицей коллекций
    @ColumnInfo(name = "id_kinopoisk") val id_kinopoisk: Long, // id_kinopoisk
    @ColumnInfo(name = "poster") val poster: String, // путь к файлу с постером во внутренней памяти
    @ColumnInfo(name = "name") val name: String,// название фильма
    @ColumnInfo(name = "genre") val genre: String,// жанр
    @ColumnInfo(name = "rate") val rate: String,// рейтинг фильма

)

