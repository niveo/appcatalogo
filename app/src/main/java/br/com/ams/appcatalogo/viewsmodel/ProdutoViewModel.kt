package br.com.ams.appcatalogo.viewsmodel

import android.os.Bundle
import androidx.lifecycle.ViewModel
import br.com.ams.appcatalogo.common.Constantes
import br.com.ams.appcatalogo.entity.Catalogo
import br.com.ams.appcatalogo.entity.Produto
import br.com.ams.appcatalogo.repository.ProdutoRepository
import com.blankj.utilcode.util.LogUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProdutoViewModel @Inject constructor(private val produtoRepository: ProdutoRepository) :
    ViewModel() {

    private var _registros = MutableStateFlow<List<Produto>>(arrayListOf())
    val registros = _registros.asStateFlow()

    fun carregarRegistros(bundle: Bundle?) {
        if (bundle == null) {
            _registros.value = produtoRepository.getAll()
        } else if (bundle.containsKey(Constantes.CATALOGO_ID) && bundle.containsKey(Constantes.CATALOGO_PAGINA_ID)) {
            _registros.value = produtoRepository.obterProdutoCatalogoPagina(
                bundle.getLong(Constantes.CATALOGO_ID),
                bundle.getLong(Constantes.CATALOGO_PAGINA_ID)
            )
        } else if (bundle.containsKey(Constantes.CATALOGO_ID)) {
            _registros.value =
                produtoRepository.obterProdutoCatalogo(bundle.getLong(Constantes.CATALOGO_ID))
        } else {
            _registros.value = produtoRepository.getAll()
        }
    }
}