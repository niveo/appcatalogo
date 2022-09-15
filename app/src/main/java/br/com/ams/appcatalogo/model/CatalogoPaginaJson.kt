package br.com.ams.appcatalogo.model

import java.util.*
import kotlin.collections.ArrayList

class CatalogoPaginaJson(
    val codigo: Long,
    val pagina: Int?,
    val dataAlterado: Date?,
    val catalogoPaginaProdutos: ArrayList<CatalogoPaginaProdutoJson>? = null
)