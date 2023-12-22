package br.com.ams.appcatalogo.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import java.util.*

class ProdutoDTO(
    var id: Long,
    var referencia: String?,
    var descricao: String?
)