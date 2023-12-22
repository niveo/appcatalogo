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


class RetrofitConfig private constructor() {

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
        val instance = RetrofitConfig()
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

        /*SPUtils.getInstance().put(
            Constantes.KEY_TOKEN_BEARER,
            "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6ImU2QUNkY2tYWWlQM0ptblloWktxaSJ9.eyJpc3MiOiJodHRwczovL2Rldi1lNjhzaTM0czVsZWNtdXBsLnVzLmF1dGgwLmNvbS8iLCJzdWIiOiJBVDZLS1BMRDdTbEh5UXg3N3dONjBES1BRUVMwelJwQ0BjbGllbnRzIiwiYXVkIjoiOWZiNDNkN2QtZDY3OS00NTFjLTg3ZWEtMTFkMDc0YjczZmM5IiwiaWF0IjoxNzAzMjcyNjM4LCJleHAiOjE3MDMzNTkwMzgsImF6cCI6IkFUNktLUExEN1NsSHlReDc3d042MERLUFFRUzB6UnBDIiwiZ3R5IjoiY2xpZW50LWNyZWRlbnRpYWxzIn0.G_7eNffO1XrGH_B8OqKdPU-Vye62y-aKBvTCuJ3KH0Nx0XCU_toPdxNzPkWSj5R7tnbxkaY4KZxQZzJHhk0N0oF-5c_E95evfxIw8hYYrHM-JkPyY_8vXsUte-BbAAHi88MpKXR51UBPavlUfhKqECVlJ_0iqhyrlCxCA-UigaisQqxlnYNx3Lf2ttcjQqPRnf2csyM0fjc3kl7Q-avsG0LWpWaT6amj8jFhro5cToe0N6MRHWQ8GtbWOPXXkCC6CqO33qtB-w7G-z16ETmjHQ8t56qA4NG1yX2ZumayxxOZhiVStkppWfG1JRxowjqKZ9CwTHWw-QeelpxjfyqnLA"
        )*/

        LogUtils.w(SPUtils.getInstance().getString(Constantes.KEY_TOKEN_BEARER))

        return OkHttpClient().newBuilder()
            .readTimeout(TIMEOUT_MILLIS.toLong(), TimeUnit.SECONDS)
            .connectTimeout(TIMEOUT_MILLIS.toLong(), TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_MILLIS.toLong(), TimeUnit.SECONDS)
            .cache(null)
            .addInterceptor(logging)
            .addInterceptor(AuthInterceptor()).build()
    }
}