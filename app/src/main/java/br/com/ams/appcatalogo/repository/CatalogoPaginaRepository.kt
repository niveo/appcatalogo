package br.com.ams.appcatalogo.repository

import br.com.ams.appcatalogo.entity.CatalogoPagina

interface CatalogoPaginaRepository {
    fun obterCatalogoPaginaMapeados(id: Long): List<CatalogoPagina>
}