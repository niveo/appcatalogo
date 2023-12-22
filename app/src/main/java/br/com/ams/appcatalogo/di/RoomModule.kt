package br.com.ams.appcatalogo.di

import android.app.Application
import androidx.room.Room
import br.com.ams.appcatalogo.ApplicationLocate
import br.com.ams.appcatalogo.dao.ProdutoDao
import br.com.ams.appcatalogo.database.AppDatabase
import br.com.ams.appcatalogo.repository.ProdutoDataSource
import br.com.ams.appcatalogo.repository.ProdutoRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule(mApplication: Application) {

    private var appDatabase: AppDatabase? = null

    init {
        appDatabase = Room.databaseBuilder(
            mApplication!!.applicationContext,
            AppDatabase::class.java, "db-catalogo"
        ).allowMainThreadQueries().build()
    }

    @Singleton
    @Provides
    fun providesRoomDatabase(): AppDatabase {
        return appDatabase!!
    }

    @Singleton
    @Provides
    fun providesProductDao(appDatabase: AppDatabase): ProdutoDao {
        return appDatabase.produtoDao()
    }

    @Singleton
    @Provides
    fun produtoRepository(produtoDao: ProdutoDao): ProdutoRepository {
        return ProdutoDataSource(produtoDao)
    }
}