package br.com.ams.appcatalogo.repository

import br.com.ams.appcatalogo.dao.CatalogoPaginaDao
import br.com.ams.appcatalogo.entity.CatalogoPagina
import javax.inject.Inject

class CatalogoPaginaDataSource @Inject constructor(private val catalogoPaginaDao: CatalogoPaginaDao) :
    CatalogoPaginaRepository {
    override fun obterCatalogoPaginaMapeados(id: Long): List<CatalogoPagina> {
        return this.catalogoPaginaDao.obterCatalogoPaginaMapeados(id)
    }
}