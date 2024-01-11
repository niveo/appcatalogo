package br.com.ams.appcatalogo.viewsmodel

import androidx.lifecycle.ViewModel
import br.com.ams.appcatalogo.entity.CatalogoPagina
import br.com.ams.appcatalogo.repository.CatalogoPaginaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CatalogoPaginaViewModel @Inject
constructor(private val catalogoPaginaRepository: CatalogoPaginaRepository) :
    ViewModel() {

    private var _registros = MutableStateFlow<List<CatalogoPagina>>(arrayListOf())
    val registros = _registros.asStateFlow()

    fun carregarCatalogoPaginaMapeados(idCatalogo: Long) {
        _registros.value = catalogoPaginaRepository.obterCatalogoPaginaMapeados(idCatalogo)
    }
}