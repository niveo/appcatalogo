package br.com.ams.appcatalogo.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.ams.appcatalogo.entity.Produto

@Dao
interface ProdutoDao : ContractDao<Produto> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun insertAll(vararg values: Produto)

    @Query("SELECT * FROM Produto")
    fun getAll(): List<Produto>

    @Query("SELECT * FROM Produto WHERE codigo IN (:codigos)")
    fun obterProdutoCodigos(codigos: List<Long>): List<Produto>
}