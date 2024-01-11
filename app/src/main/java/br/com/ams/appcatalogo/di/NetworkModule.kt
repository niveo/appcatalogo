package br.com.ams.appcatalogo.di

import br.com.ams.appcatalogo.annotation.AuthInterceptorOkHttpClient
import br.com.ams.appcatalogo.common.Config
import br.com.ams.appcatalogo.common.Constantes
import br.com.ams.appcatalogo.enuns.TipoTimeOutConexao
import br.com.ams.appcatalogo.retrofit.ICargaArquivo
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SPUtils
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor() = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

    @Singleton
    @Provides
    fun providesHttpAuthrizationInterceptor() = Interceptor {
        val requestBuilder = it.request().newBuilder()
        val token = "Bearer " + SPUtils.getInstance().getString(Constantes.KEY_TOKEN_BEARER)
        val userId = SPUtils.getInstance().getString(Constantes.KEY_TOKEN_USER_ID)
        requestBuilder.addHeader("Authorization", token)
        requestBuilder.addHeader("userid", userId)
        it.proceed(requestBuilder.build())
    }

    @Singleton
    @Provides
    fun providesHttpTimeOut(): Long {
        val tipoTimeOutConexao: TipoTimeOutConexao = TipoTimeOutConexao.MEDIA
        return when (tipoTimeOutConexao) {
            TipoTimeOutConexao.CURTA -> 5L
            TipoTimeOutConexao.MEDIA -> 15
            TipoTimeOutConexao.LONGA -> 30
            else -> 10
        }
    }


    @Singleton
    @AuthInterceptorOkHttpClient
    @Provides
    fun provideAuthInterceptorOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        httpAuthrizationInterceptor: Interceptor,
        providesHttpTimeOut: Long
    ): OkHttpClient {

        LogUtils.v("TIME OUT", providesHttpTimeOut)

        return OkHttpClient().newBuilder()
            .readTimeout(providesHttpTimeOut, TimeUnit.SECONDS)
            .connectTimeout(providesHttpTimeOut, TimeUnit.SECONDS)
            .writeTimeout(providesHttpTimeOut, TimeUnit.SECONDS)
            .cache(null)
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(httpAuthrizationInterceptor).build()
    }

    @Singleton
    @Provides
    fun provideAnalyticsService(
        @AuthInterceptorOkHttpClient okHttpClient: OkHttpClient
    ): Retrofit {
        val gsonBuilder = GsonBuilder()
        val gson: Gson = gsonBuilder.create()

        return Retrofit.Builder()
            .baseUrl(Config.URL_SERVIDOR + "/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideCargaArquivo(retrofit: Retrofit): ICargaArquivo {
        return retrofit.create(ICargaArquivo::class.java)
    }

}