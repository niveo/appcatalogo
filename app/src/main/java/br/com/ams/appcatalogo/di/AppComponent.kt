package br.com.ams.appcatalogo.di

import br.com.ams.appcatalogo.MainActivity
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component( modules = [AppModule::class, RoomModule::class])
interface AppComponent  {
    fun inject(mainActivity: MainActivity?)

    /*fun productDao(): ProdutoDao

    fun appDatabase(): AppDatabase

    fun application(): Application*/
}