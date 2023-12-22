package br.com.ams.appcatalogo.retrofit

import br.com.ams.appcatalogo.ApplicationLocate
import br.com.ams.appcatalogo.common.Config
import br.com.ams.appcatalogo.common.Constantes
import br.com.ams.appcatalogo.enuns.TipoTimeOutConexao
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SPUtils
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class RetrofitConfig private constructor(){

    private fun getRetrofit(): Retrofit {

        val gsonBuilder = GsonBuilder()
        //gsonBuilder.registerTypeAdapter(CatalogoPagina::class.java, CatalogoPaginaDeserializer())
        val gson: Gson = gsonBuilder.create()

        return Retrofit.Builder()
            .baseUrl(Config.URL_SERVIDOR + "/")
            .client(getUnsafeOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()

    }

    companion object {
        lateinit var instance: RetrofitConfig
            private set
    }

    fun getCargaArquivo(): ICargaArquivo {
        return this.getRetrofit().create(ICargaArquivo::class.java)
    }

    private fun getUnsafeOkHttpClient(tipoTimeOutConexao: TipoTimeOutConexao = TipoTimeOutConexao.MEDIA): OkHttpClient {

        val TIMEOUT_MILLIS: Int

        when (tipoTimeOutConexao) {
            TipoTimeOutConexao.PREFERENCIA -> {
                TIMEOUT_MILLIS = SPUtils.getInstance().getInt(
                    Constantes.KEY_TIME_OUT_CONEXAO,
                    Constantes.TIME_OUT_CONEXAO
                )
            }
            TipoTimeOutConexao.CURTA -> {
                TIMEOUT_MILLIS = 5
            }
            TipoTimeOutConexao.MEDIA -> {
                TIMEOUT_MILLIS = 15
            }
            TipoTimeOutConexao.LONGA -> {
                TIMEOUT_MILLIS = 30
            }
            else -> {
                TIMEOUT_MILLIS = 10
            }
        }

        LogUtils.v("TIME OUT", TIMEOUT_MILLIS)

        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)

        return OkHttpClient().newBuilder()
            .authenticator({ route, response ->
                val token = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6ImU2QUNkY2tYWWlQM0ptblloWktxaSJ9.eyJpc3MiOiJodHRwczovL2Rldi1lNjhzaTM0czVsZWNtdXBsLnVzLmF1dGgwLmNvbS8iLCJzdWIiOiJBVDZLS1BMRDdTbEh5UXg3N3dONjBES1BRUVMwelJwQ0BjbGllbnRzIiwiYXVkIjoiOWZiNDNkN2QtZDY3OS00NTFjLTg3ZWEtMTFkMDc0YjczZmM5IiwiaWF0IjoxNzAzMTY0ODY3LCJleHAiOjE3MDMyNTEyNjcsImF6cCI6IkFUNktLUExEN1NsSHlReDc3d042MERLUFFRUzB6UnBDIiwiZ3R5IjoiY2xpZW50LWNyZWRlbnRpYWxzIn0.Ve3s7I3sSjEj4Db20l7So-y6k0J0pjomnslc1A7VO1SY9cnVt6ehMHK0qhCrLwPCbvGRBa3fazy3q-r4qkYMh_IlS0Eh_tO_PHKaZqIPag40vyg8WisAyqMepry8bYrEoz0vltjZKY9eyFvXLJh-eIwASq-aH9PvLnX1-ZcLoWmKyAKODxEqUyWKo8tyxnDwOwTYnBq25Y3ApMGz-9J6j8Y4Boei0aui7rnhTRv2DYLAH6JZ19fm9UQkpKzAxWtGhpD6Hut3uXVfKa-fXhOibIeaapHSQ6F-qD55htcdsuNtxsx_FSbRVgY0lp0ofepEFkYbSVoeHUOVQsUTA90zrQ"
                response.request.newBuilder().header("authorization", token).build()
            })
            .readTimeout(TIMEOUT_MILLIS.toLong(), TimeUnit.SECONDS)
            .connectTimeout(TIMEOUT_MILLIS.toLong(), TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_MILLIS.toLong(), TimeUnit.SECONDS)
            .cache(null)
            .addInterceptor(logging).build()
    }
}