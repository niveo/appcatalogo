package br.com.ams.appcatalogo.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.ams.appcatalogo.entity.Produto

@Dao
interface ProdutoDao  {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg values: Produto)

    @Query("SELECT * FROM Produto")
    fun getAll(): LiveData<Produto>

    @Query("SELECT * FROM Produto WHERE id IN (:ids)")
    fun obterProdutoCodigos(ids: List<Long>): LiveData<Produto>
}