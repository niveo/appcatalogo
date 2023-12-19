package br.com.ams.appcatalogo.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.ams.appcatalogo.entity.Catalogo
import br.com.ams.appcatalogo.model.CatalogoMapeadosDTO

@Dao
interface CatalogoDao : ContractDao<Catalogo> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun insertAll(vararg values: Catalogo)

    @Query("SELECT C.codigo, C.descricao, C.imagemUrl, C.observacao, C.dataAlterado," +
            "(SELECT COUNT(*) FROM CatalogoPaginaProduto AS CPP WHERE CPP.codigoCatalogoPagina = (SELECT CP.codigo FROM CatalogoPagina AS CP WHERE CP.codigoCatalogo = C.codigo)) as mapeados" +
            " FROM Catalogo AS C")
    fun obterCatalogoMapeados(): List<CatalogoMapeadosDTO>
}