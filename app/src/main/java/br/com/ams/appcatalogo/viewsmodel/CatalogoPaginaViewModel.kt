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

    private var _codigo: Long? = null
    val codigo: Long
        get() = _codigo!!

    private var _descricao: String? = null
    val descricao: String
        get() = _descricao!!

    private var _identificador: String? = null
    val identificador: String
        get() = _identificador!!

    private var _registros = MutableStateFlow<List<CatalogoPagina>>(arrayListOf())
    val registros = _registros.asStateFlow()

    fun carregarDadosView(codigo: Long, descricao: String, identificador: String) {
        this._codigo = codigo;
        this._descricao = descricao;
        this._identificador = identificador;
        _registros.value = catalogoPaginaRepository.obterCatalogoPaginaMapeados(this.codigo)
    }
}