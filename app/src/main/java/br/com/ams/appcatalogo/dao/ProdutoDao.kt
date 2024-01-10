package br.com.ams.appcatalogo.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.ams.appcatalogo.entity.Produto
import kotlinx.coroutines.flow.Flow

private const val QUERY_PRODUTOS_DO_CATALOGO = "select cpmpp.produtoId from catalogo, " +
        "catalogo_pagina as cp, " +
        "catalogo_pagina_mapeamento as cpm,  " +
        "catalogo_pagina_mapeamento_produtos_produto as cpmpp " +
        "where catalogo.id = :idCatalogo AND cp.catalogoId = catalogo.id AND cpm.catalogoPaginaId = cp.id " +
        "AND cpmpp.catalogoPaginaMapeamentoId = cpm.id"

private const val QUERY_PRODUTOS_DO_CATALOGO_PAGINA = "select cpmpp.produtoId from catalogo, " +
        "catalogo_pagina as cp, " +
        "catalogo_pagina_mapeamento as cpm,  " +
        "catalogo_pagina_mapeamento_produtos_produto as cpmpp " +
        "where catalogo.id = :idCatalogo AND cp.id = :idCatalogoPagina AND cp.catalogoId = catalogo.id AND cpm.catalogoPaginaId = cp.id " +
        "AND cpmpp.catalogoPaginaMapeamentoId = cpm.id"

@Dao
interface ProdutoDao {

    @Query("SELECT * FROM produto WHERE ativo = 1")
    fun getAll(): List<Produto>

    @Query("SELECT * FROM produto WHERE id IN (:ids) and ativo = 1")
    fun obterProdutoCodigos(ids: List<Long>): List<Produto>


    @Query("SELECT * FROM produto WHERE id IN ($QUERY_PRODUTOS_DO_CATALOGO)")
    fun obterProdutoCatalogo(idCatalogo: Long): List<Produto>

    @Query("SELECT * FROM produto WHERE id IN ($QUERY_PRODUTOS_DO_CATALOGO_PAGINA)")
    fun obterProdutoCatalogoPagina(idCatalogo: Long, idCatalogoPagina: Long): List<Produto>
}