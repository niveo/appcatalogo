package br.com.ams.appcatalogo.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Catalogo")
class Catalogo(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "descricao") val descricao: String?,
    @ColumnInfo(name = "titulo") val titulo: String?,
    @ColumnInfo(name = "logo") val logo: String?,
    @ColumnInfo(name = "avatar") val avatar: String?,
)