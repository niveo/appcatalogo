package br.com.ams.appcatalogo.repository

import androidx.lifecycle.LiveData
import br.com.ams.appcatalogo.dao.ProdutoDao
import br.com.ams.appcatalogo.entity.Produto
import javax.inject.Inject

class ProdutoDataSource @Inject constructor( var produtoDao: ProdutoDao) : ProdutoRepository {
    override fun obterProdutoCodigos(ids: List<Long>): LiveData<Produto> {
        return this.produtoDao.obterProdutoCodigos(ids)
    }

    override fun getAll(): LiveData<Produto> {
        return this.produtoDao.getAll()
    }

    override fun insertAll(values: Produto) {
        return this.produtoDao.insertAll(values)
    }
}