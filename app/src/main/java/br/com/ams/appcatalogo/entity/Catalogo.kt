package br.com.ams.appcatalogo.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*
import kotlin.collections.ArrayList

@Entity(tableName = "Catalogo")
class Catalogo(
    @PrimaryKey val codigo: Long,
    @ColumnInfo(name = "descricao") val descricao: String?,
    @ColumnInfo(name = "observacao") val observacao: String?,
    @ColumnInfo(name = "imagemUrl") val imagemUrl: String?,
    @ColumnInfo(name = "dataCadastrado") var dataCadastrado: Date? = null,
    @ColumnInfo(name = "dataAlterado") var dataAlterado: Date? = null
)