package br.com.ams.appcatalogo.repository

import androidx.lifecycle.LiveData
import br.com.ams.appcatalogo.dao.ProdutoDao
import br.com.ams.appcatalogo.entity.Produto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProdutoDataSource @Inject constructor(private val produtoDao: ProdutoDao) : ProdutoRepository {
    override fun obterProdutoCodigos(ids: List<Long>): List<Produto> {
        return this.produtoDao.obterProdutoCodigos(ids)
    }

    override fun getAll(): List<Produto> {
        return this.produtoDao.getAll()
    }
}