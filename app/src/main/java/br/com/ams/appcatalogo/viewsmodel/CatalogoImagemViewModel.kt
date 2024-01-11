package br.com.ams.appcatalogo.viewsmodel

import android.graphics.Bitmap
import android.graphics.PointF
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.ams.appcatalogo.ApplicationLocate
import br.com.ams.appcatalogo.common.Constantes
import br.com.ams.appcatalogo.entity.CatalogoPagina
import br.com.ams.appcatalogo.repository.ProdutoRepository
import com.blankj.utilcode.util.LogUtils
import com.example.imagekit.android.picasso_extension.createWithPicasso
import com.imagekit.android.ImageKit
import com.imagekit.android.entity.TransformationPosition
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatalogoImagemViewModel @Inject constructor(private val produtoRepository: ProdutoRepository) :
    ViewModel() {

    private var _imagem = MutableStateFlow<Bitmap?>(null)
    val imagem = _imagem.asStateFlow()

    private var _registrosCordenadas = MutableStateFlow(longArrayOf())
    val registrosCordenadas = _registrosCordenadas.asStateFlow()

    fun carregarImagem(nome: String, identificador: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _imagem.value = ImageKit.getInstance()
                .url(
                    path = nome,
                    transformationPosition = TransformationPosition.QUERY,
                    urlEndpoint = "${ApplicationLocate.instance.dotenv[Constantes.IMAGEKIT_ENDPOINT]}/catalogo/catalogos/${identificador}/paginas/"
                )
                .createWithPicasso()
                .get()
        }
    }

    fun carregarRegistrosCordenadas(
        codigoCatalogo: Long,
        codigoCatalogoPagina: Long,
        sCoord: PointF
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val cursor = produtoRepository
                .obterProdutosCordenada(
                    codigoCatalogo,
                    codigoCatalogoPagina,
                    sCoord.x,
                    sCoord.y
                )
            val registros = ArrayList<Long>()
            with(cursor) {
                this.use {
                    while (it.moveToNext()) {
                        registros.add(it.getLong(0))
                    }
                }
            }
            _registrosCordenadas.value = registros.toLongArray()
        }
    }
}