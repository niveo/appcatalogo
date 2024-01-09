package br.com.ams.appcatalogo.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.ams.appcatalogo.entity.Produto
import kotlinx.coroutines.flow.Flow

@Dao
interface ProdutoDao  {

    @Query("SELECT * FROM produto WHERE ativo = 1")
    fun getAll(): List<Produto>

    @Query("SELECT * FROM produto WHERE id IN (:ids) and ativo = 1")
    fun obterProdutoCodigos(ids: List<Long>): List<Produto>
}