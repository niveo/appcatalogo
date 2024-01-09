package br.com.ams.appcatalogo.repository

import androidx.lifecycle.LiveData
import br.com.ams.appcatalogo.entity.Catalogo
import kotlinx.coroutines.flow.Flow

interface CatalogoRepository {
    fun getAll(): List<Catalogo>
}