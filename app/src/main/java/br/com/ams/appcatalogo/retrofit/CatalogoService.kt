package br.com.ams.appcatalogo.retrofit

import br.com.ams.appcatalogo.model.CatalogoJson
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

interface CatalogoService {
    @GET("/catalogo")
    fun obterTodos(): Observable<List<CatalogoJson>>
}