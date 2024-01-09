package br.com.ams.appcatalogo.repository

import androidx.lifecycle.LiveData
import br.com.ams.appcatalogo.entity.Produto
import kotlinx.coroutines.flow.Flow

interface ProdutoRepository {
    fun obterProdutoCodigos(ids: List<Long>): List<Produto>
    fun getAll(): List<Produto>
}