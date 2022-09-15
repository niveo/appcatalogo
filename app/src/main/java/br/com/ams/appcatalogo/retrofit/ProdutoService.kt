package br.com.ams.appcatalogo.retrofit
import br.com.ams.appcatalogo.model.ProdutoJson
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

interface ProdutoService {
    @GET("/produto")
    fun obterTodos(): Observable<List<ProdutoJson>>
}