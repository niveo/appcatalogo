package br.com.ams.appcatalogo.dao

import androidx.room.Dao
import androidx.room.Query
import br.com.ams.appcatalogo.entity.CatalogoPagina

@Dao
interface CatalogoPaginaDao  {
    @Query("SELECT * FROM catalogo_pagina where catalogoId = :id ORDER BY pagina")
    fun obterCatalogoPaginaMapeados(id: Long): List<CatalogoPagina>
}