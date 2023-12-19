package br.com.ams.appcatalogo.model

class CatalogoPaginaProdutoJson(
    val codigo: Long,
    val inicialPosicalX: Float?,
    val finalPosicalX: Float?,
    val inicialPosicalY: Float?,
    val finalPosicalY: Float?,
    val width: Float?,
    val height: Float?,
    val produto: ProdutoDTO
)