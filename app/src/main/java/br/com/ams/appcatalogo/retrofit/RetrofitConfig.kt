package br.com.ams.appcatalogo.retrofit

import br.com.ams.appcatalogo.common.Config
import br.com.ams.appcatalogo.common.Constantes
import br.com.ams.appcatalogo.enuns.TipoTimeOutConexao
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SPUtils
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Authenticator
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class RetrofitConfig {

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

    fun getCatalogoService(): CatalogoService {
        return this.getRetrofit().create(CatalogoService::class.java)
    }

    fun getProdutoService(): ProdutoService {
        return this.getRetrofit().create(ProdutoService::class.java)
    }

    fun getCatalogoArquivoService(): CatalogoArquivoService {
        return this.getRetrofit().create(CatalogoArquivoService::class.java)
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
                val credential = Credentials.basic("admin", "admin")
                response.request.newBuilder().header("Authorization", credential).build()
            })
            .readTimeout(TIMEOUT_MILLIS.toLong(), TimeUnit.SECONDS)
            .connectTimeout(TIMEOUT_MILLIS.toLong(), TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_MILLIS.toLong(), TimeUnit.SECONDS)
            .cache(null)
            .addInterceptor(logging).build()
    }
}