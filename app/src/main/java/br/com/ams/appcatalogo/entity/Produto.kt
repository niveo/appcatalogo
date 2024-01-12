package br.com.ams.appcatalogo.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "produto")
data class Produto(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "referencia") val referencia: String?,
    @ColumnInfo(name = "descricao") val descricao: String?,
    @ColumnInfo(name = "valor") val valor: Double?,
    @ColumnInfo(name = "ativo") val ativo: Boolean?
)