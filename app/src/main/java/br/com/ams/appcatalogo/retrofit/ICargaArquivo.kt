package br.com.ams.appcatalogo.retrofit

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET

interface ICargaArquivo {
    @GET("/mobile/buffer")
    fun obterCarga(): Call<ResponseBody>
}