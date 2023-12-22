package br.com.ams.appcatalogo.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.ams.appcatalogo.entity.CatalogoPagina
import br.com.ams.appcatalogo.model.CatalogoPaginaDTO
import br.com.ams.appcatalogo.model.CatalogoPaginaMapeadosDTO

@Dao
interface CatalogoPaginaDao  {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
      fun insertAll(vararg values: CatalogoPagina)

    @Query("SELECT * FROM catalogopagina where id = :id")
    fun obterCatalogoPaginaMapeados(id: Long): List<CatalogoPaginaDTO>?
}