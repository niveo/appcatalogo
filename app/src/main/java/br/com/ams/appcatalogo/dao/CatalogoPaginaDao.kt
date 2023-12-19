package br.com.ams.appcatalogo.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.ams.appcatalogo.entity.CatalogoPagina
import br.com.ams.appcatalogo.model.CatalogoPaginaMapeadosDTO

@Dao
interface CatalogoPaginaDao : ContractDao<CatalogoPagina> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun insertAll(vararg values: CatalogoPagina)

    @Query("SELECT CP.codigo, CP.codigoCatalogo, CP.pagina, " +
            "CP.dataAlterado, (SELECT COUNT(*) FROM CatalogoPaginaProduto AS CPP WHERE CPP.codigoCatalogoPagina = CP.codigo) as contaMapeados " +
            "from CatalogoPagina AS CP where CP.codigoCatalogo = :codigo")
    fun obterCatalogoPaginaMapeados(codigo: Long): List<CatalogoPaginaMapeadosDTO>?
}