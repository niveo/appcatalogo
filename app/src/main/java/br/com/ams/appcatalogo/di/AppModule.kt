package br.com.ams.appcatalogo.di

import android.app.Application
import br.com.ams.appcatalogo.ApplicationLocate
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class AppModule(application: Application) {
    var mApplication: Application? = null

    init {
        mApplication = application
    }

    @Provides
    @Singleton
    fun providesApplication(): Application {
        return mApplication!!
    }
}