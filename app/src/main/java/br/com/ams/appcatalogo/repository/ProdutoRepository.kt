package br.com.ams.appcatalogo.repository

import androidx.lifecycle.LiveData
import br.com.ams.appcatalogo.entity.Produto

interface ProdutoRepository {
    fun obterProdutoCodigos(ids: List<Long>): LiveData<Produto>
    fun getAll(): LiveData<Produto>
    fun insertAll(  values: Produto)
}