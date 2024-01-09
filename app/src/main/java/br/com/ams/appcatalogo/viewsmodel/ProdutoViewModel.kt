package br.com.ams.appcatalogo.viewsmodel

import androidx.lifecycle.ViewModel
import br.com.ams.appcatalogo.repository.ProdutoRepository

class ProdutoViewModel(private val produtoRepository: ProdutoRepository) :
    ViewModel() {
    val registros = produtoRepository.getAll()
}