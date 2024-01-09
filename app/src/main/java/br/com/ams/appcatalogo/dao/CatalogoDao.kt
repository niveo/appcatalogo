package br.com.ams.appcatalogo.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.ams.appcatalogo.entity.Catalogo
import br.com.ams.appcatalogo.entity.Produto
import br.com.ams.appcatalogo.model.CatalogoDTO
import kotlinx.coroutines.flow.Flow

@Dao
interface CatalogoDao {
    @Query("SELECT * FROM catalogo WHERE ativo = 1")
    fun getAll(): List<Catalogo>
}