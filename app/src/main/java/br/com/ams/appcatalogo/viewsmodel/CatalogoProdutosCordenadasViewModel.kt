package br.com.ams.appcatalogo.viewsmodel

import androidx.lifecycle.ViewModel
import br.com.ams.appcatalogo.entity.Produto
import br.com.ams.appcatalogo.repository.ProdutoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CatalogoProdutosCordenadasViewModel @Inject constructor(
    private val produtoRepository: ProdutoRepository
) : ViewModel() {

    private var _registros = MutableStateFlow<List<Produto>>(arrayListOf())
    val registros = _registros.asStateFlow()

    fun carregarRegistros(ids: LongArray?) {
        _registros.value = produtoRepository.obterProdutoCodigos(ids!!.asList())
    }
}