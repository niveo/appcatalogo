package br.com.ams.appcatalogo.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.ams.appcatalogo.dao.CatalogoDao
import br.com.ams.appcatalogo.dao.CatalogoPaginaDao
import br.com.ams.appcatalogo.dao.ProdutoDao
import br.com.ams.appcatalogo.entity.Catalogo
import br.com.ams.appcatalogo.entity.CatalogoPagina
import br.com.ams.appcatalogo.entity.CatalogoPaginaMapeamento
import br.com.ams.appcatalogo.entity.CatalogoPaginaMapeamentoProduto
import br.com.ams.appcatalogo.entity.Produto



const val VERSION_DB = 2

@Database(
    entities = [
        Produto::class,
        Catalogo::class,
        CatalogoPagina::class,
        CatalogoPaginaMapeamento::class,
        CatalogoPaginaMapeamentoProduto::class
    ],
    version = VERSION_DB
)
@TypeConverters(value = [Converters::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun produtoDao(): ProdutoDao
    abstract fun catalogoDao(): CatalogoDao
    abstract fun catalogoPaginaDao(): CatalogoPaginaDao
}