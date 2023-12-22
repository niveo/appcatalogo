package br.com.ams.appcatalogo.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.ams.appcatalogo.entity.Catalogo
import br.com.ams.appcatalogo.model.CatalogoDTO

@Dao
interface CatalogoDao  {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
      fun insertAll(vararg values: Catalogo)

    @Query("")
    fun carregarCatalogos(): List<CatalogoDTO>
}