package br.com.ams.appcatalogo.repository

import android.database.Cursor
import br.com.ams.appcatalogo.entity.Produto

interface ProdutoRepository {
    fun obterProdutoCodigos(ids: List<Long>): List<Produto>

    fun obterProdutoCatalogo(idCatalogo: Long): List<Produto>

    fun obterProdutoCatalogoPagina(idCatalogo: Long, idCatalogoPagina: Long): List<Produto>

    fun getAll(): List<Produto>

    fun obterProdutosCordenada(
        codigoCatalogo: Long,
        codigoCatalogoPagina: Long,
        x: Float,
        y: Float
    ): Cursor
}