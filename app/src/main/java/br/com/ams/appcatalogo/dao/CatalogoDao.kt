package br.com.ams.appcatalogo.dao

import androidx.room.Dao
import androidx.room.Query
import br.com.ams.appcatalogo.entity.Catalogo

@Dao
interface CatalogoDao {
    @Query("SELECT * FROM catalogo WHERE ativo = 1")
    fun getAll(): List<Catalogo>
}