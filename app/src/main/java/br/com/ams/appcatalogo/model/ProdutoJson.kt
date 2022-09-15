package br.com.ams.appcatalogo.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import br.com.ams.appcatalogo.entity.Produto
import java.util.*

class ProdutoJson(
    val codigo: Long,
    val referencia: String?,
    val descricao: String?,
    var dataCadastrado: Date? = null,
    var dataAlterado: Date? = null,
    var valor: Double? = null
)