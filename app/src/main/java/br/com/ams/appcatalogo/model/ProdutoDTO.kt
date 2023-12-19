package br.com.ams.appcatalogo.model

import java.util.*

class ProdutoDTO(
    val codigo: Long,
    val referencia: String?,
    val descricao: String?,
    var dataCadastrado: Date? = null,
    var dataAlterado: Date? = null,
    var valor: Double? = null
)