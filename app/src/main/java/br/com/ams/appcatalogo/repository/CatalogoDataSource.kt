package br.com.ams.appcatalogo.repository

import androidx.lifecycle.LiveData
import br.com.ams.appcatalogo.dao.CatalogoDao
import br.com.ams.appcatalogo.entity.Catalogo
import br.com.ams.appcatalogo.entity.Produto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CatalogoDataSource @Inject constructor(private val catalogoDao: CatalogoDao) :
    CatalogoRepository {

    override fun getAll(): List<Catalogo> {
        return this.catalogoDao.getAll()
    }

}