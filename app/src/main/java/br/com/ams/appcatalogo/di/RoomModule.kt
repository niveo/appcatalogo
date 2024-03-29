package br.com.ams.appcatalogo.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import br.com.ams.appcatalogo.ApplicationLocate
import br.com.ams.appcatalogo.dao.CatalogoDao
import br.com.ams.appcatalogo.dao.CatalogoPaginaDao
import br.com.ams.appcatalogo.dao.ProdutoDao
import br.com.ams.appcatalogo.database.AppDatabase
import br.com.ams.appcatalogo.repository.CatalogoDataSource
import br.com.ams.appcatalogo.repository.CatalogoPaginaDataSource
import br.com.ams.appcatalogo.repository.CatalogoPaginaRepository
import br.com.ams.appcatalogo.repository.CatalogoRepository
import br.com.ams.appcatalogo.repository.ProdutoDataSource
import br.com.ams.appcatalogo.repository.ProdutoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {

    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE catalogo ADD COLUMN identificador TEXT")
        }
    }

    @Singleton
    @Provides
    fun providesRoomDatabase(@ApplicationContext appContext: Context) = Room.databaseBuilder(
        appContext,
        AppDatabase::class.java, "db-catalogo"
    ).allowMainThreadQueries()
        .addMigrations(MIGRATION_1_2)
        .build()

    @Singleton
    @Provides
    fun providesProdutoDao(appDatabase: AppDatabase): ProdutoDao {
        return appDatabase.produtoDao()
    }

    @Singleton
    @Provides
    fun produtoRepository(produtoDao: ProdutoDao): ProdutoRepository {
        return ProdutoDataSource(produtoDao)
    }

    @Singleton
    @Provides
    fun providesCatalogoDao(appDatabase: AppDatabase): CatalogoDao {
        return appDatabase.catalogoDao()
    }

    @Singleton
    @Provides
    fun catalogoRepository(catalogoDao: CatalogoDao): CatalogoRepository {
        return CatalogoDataSource(catalogoDao)
    }

    @Singleton
    @Provides
    fun providesCatalogoPaginaDao(appDatabase: AppDatabase): CatalogoPaginaDao {
        return appDatabase.catalogoPaginaDao()
    }

    @Singleton
    @Provides
    fun catalogoPaginaRepository(catalogoPaginaDao: CatalogoPaginaDao): CatalogoPaginaRepository {
        return CatalogoPaginaDataSource(catalogoPaginaDao)
    }
}