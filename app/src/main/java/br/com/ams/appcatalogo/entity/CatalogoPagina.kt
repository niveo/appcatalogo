package br.com.ams.appcatalogo.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "CatalogoPagina")
class CatalogoPagina(
    @PrimaryKey val codigo: Long,
    @ColumnInfo(name = "pagina") val pagina: Int?,
    @ColumnInfo(name = "codigoCatalogo") val codigoCatalogo: Long?,
    @ColumnInfo(name = "dataAlterado") val dataAlterado: Date? = null,
)