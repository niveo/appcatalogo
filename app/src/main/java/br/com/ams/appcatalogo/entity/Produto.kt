package br.com.ams.appcatalogo.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "Produto")
class Produto(
    @PrimaryKey val codigo: Long,
    @ColumnInfo(name = "referencia") val referencia: String?,
    @ColumnInfo(name = "descricao") val descricao: String?,
    @ColumnInfo(name = "dataCadastrado") var dataCadastrado: Date? = null,
    @ColumnInfo(name = "dataAlterado") var dataAlterado: Date? = null,
    @ColumnInfo(name = "valor") var valor: Double? = null,
)