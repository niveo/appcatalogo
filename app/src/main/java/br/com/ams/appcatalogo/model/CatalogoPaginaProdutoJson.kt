package br.com.ams.appcatalogo.model
import br.com.ams.appcatalogo.entity.CatalogoPagina

class CatalogoPaginaProdutoJson(
    val codigo: Long,
    val inicialPosicalX: Float?,
    val finalPosicalX: Float?,
    val inicialPosicalY: Float?,
    val finalPosicalY: Float?,
    val width: Float?,
    val height: Float?,
    val produto: ProdutoJson
)