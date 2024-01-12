package br.com.ams.appcatalogo.repository

import br.com.ams.appcatalogo.entity.Catalogo

interface CatalogoRepository {
    fun getAll(): List<Catalogo>
}