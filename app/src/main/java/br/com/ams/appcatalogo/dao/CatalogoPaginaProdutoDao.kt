package br.com.ams.appcatalogo.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.ams.appcatalogo.entity.CatalogoPaginaMapeamento

@Dao
interface CatalogoPaginaProdutoDao   {
   /* @Query("SELECT * FROM catalogopaginamapeamentoproduto")
    fun obterIdProduto(
        codigoCatalogo: Long,
        codigoCatalogoPagina: Long,
        posicaoInicial: Float,
        posicaoFinal: Float
    ): LiveData<Long>*/
}