package br.com.ams.appcatalogo.model

import java.util.*
import kotlin.collections.ArrayList

class CatalogoPaginaDTO(
    val codigo: Long,
    val pagina: Int?,
    val dataAlterado: Date?,
    val mapeados: ArrayList<CatalogoPaginaProdutoJson>? = null
)