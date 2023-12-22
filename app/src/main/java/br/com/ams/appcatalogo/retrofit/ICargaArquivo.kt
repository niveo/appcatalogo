package br.com.ams.appcatalogo.retrofit

import br.com.ams.appcatalogo.model.ProdutoDTO
import io.reactivex.rxjava3.core.Observable
import retrofit2.Call
import okhttp3.ResponseBody
import retrofit2.http.GET

interface ICargaArquivo {
    @GET("/mobile/buffer")
    fun obterCarga(): Call<ResponseBody>
}