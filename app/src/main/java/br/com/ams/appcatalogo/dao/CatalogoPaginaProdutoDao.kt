package br.com.ams.appcatalogo.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.ams.appcatalogo.entity.CatalogoPaginaProduto

@Dao
interface CatalogoPaginaProdutoDao : ContractDao<CatalogoPaginaProduto> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun insertAll(vararg values: CatalogoPaginaProduto)

    @Query(
        "SELECT codigoProduto FROM CatalogoPaginaProduto as CPP " +
                "INNER JOIN CatalogoPagina AS CP ON CP.codigo = CPP.codigoCatalogoPagina " +
                "INNER JOIN Catalogo AS C ON C.codigo = CP.codigoCatalogo " +
                "where CP.codigo = :codigoCatalogoPagina AND CP.codigoCatalogo = :codigoCatalogo " +
                "AND (CPP.finalPosicalX >= :posicaoInicial AND CPP.inicialPosicalX <= :posicaoInicial) " +
                "AND (CPP.finalPosicalY >= :posicaoFinal AND CPP.inicialPosicalY <= :posicaoFinal)"
    )
    fun obterIdProduto(
        codigoCatalogo: Long,
        codigoCatalogoPagina: Long,
        posicaoInicial: Float,
        posicaoFinal: Float
    ): Array<Long>
}