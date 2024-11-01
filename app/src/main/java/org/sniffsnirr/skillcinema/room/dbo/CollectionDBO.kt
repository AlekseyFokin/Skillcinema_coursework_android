package org.sniffsnirr.skillcinema.room.dbo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

// модель коллекции фильмов
@Entity(
    tableName = "collection",
    indices = [Index("id")]
)
data class CollectionDBO(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long,// id
    @ColumnInfo(name = "name") val name: String,// название
    @ColumnInfo(name = "embedded") val embedded: Int //метка 0 - коллекция пользователя, 1 - встроенная коллекция
)
