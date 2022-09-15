package br.com.ams.appcatalogo.retrofit

import io.reactivex.rxjava3.core.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CatalogoArquivoService {
    @GET("/catalogoarquivo/download/{codigo}")
    fun downloadCatalogo(@Path("codigo") codigo: Long): Observable<ResponseBody>


    @GET("/catalogoarquivo/pagina/download/{codigoCatalogo}")
    fun downloadCatalogoPaginas(
        @Path("codigoCatalogo") codigoCatalogo: Long,
        @Query("codigoCatalogoPaginas") codigoCatalogoPaginas: String
    ): Observable<ResponseBody>
}