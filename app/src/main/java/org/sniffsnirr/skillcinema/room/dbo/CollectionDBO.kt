package org.sniffsnirr.skillcinema.room.dbo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "collection",
    indices = [Index("id")]
)
data class CollectionDBO(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "embedded") val embedded: Int
)
