package br.com.ams.appcatalogo.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "CatalogoPagina")
class CatalogoPagina(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "catalogoId") val catalogoId: Long?,
    @ColumnInfo(name = "pagina") val pagina: Int?,
    @ColumnInfo(name = "size") val size: Int?,
    @ColumnInfo(name = "height") val height: Int?,
    @ColumnInfo(name = "width") val width: Int?,
    @ColumnInfo(name = "name") val name: String?,
)