package br.com.ams.appcatalogo.catalogo

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import br.com.ams.appcatalogo.R
import br.com.ams.appcatalogo.catalogo.utils.UtilCatalogo
import br.com.ams.appcatalogo.viewsmodel.CatalogoPaginaViewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.ams.appcatalogo.ApplicationLocate
import br.com.ams.appcatalogo.common.Constantes
import br.com.ams.appcatalogo.entity.CatalogoPagina
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest

private const val PARAM_CODIGO: String = "PARAM_CODIGO"
private const val PARAM_DESCRICAO: String = "PARAM_DESCRICAO"
private const val PARAM_IDENTIFICADOR: String = "PARAM_IDENTIFICADOR"

class CatalogoPaginaFragment : DialogFragment() {


    private val viewModel: CatalogoPaginaViewModel by activityViewModels()

    companion object {
        private const val DIALOG_TAG = "CatalogoPaginaFragment"

        @JvmStatic
        fun newInstance(codigo: Long, descricao: String?, identificador: String) =
            CatalogoPaginaFragment().apply {
                arguments = bundleOf(
                    PARAM_CODIGO to codigo,
                    PARAM_DESCRICAO to descricao,
                    PARAM_IDENTIFICADOR to identificador
                )
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            viewModel.carregarDadosView(
                it.getLong(PARAM_CODIGO),
                it.getString(PARAM_DESCRICAO, ""),
                it.getString(PARAM_IDENTIFICADOR)!!
            )
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme {
                    CatalogoPaginaView { registro ->
                        CatalogoImagemFragment.newInstance(
                            viewModel.identificador,
                            registro.name!!,
                            registro.catalogoId!!,
                            registro.id,
                        ).openDialog(parentFragmentManager)
                    }
                }
            }
        }
    }


    @Composable
    fun CatalogoPaginaView(onItemClick: (registro: CatalogoPagina) -> Unit) {
        val descricao = viewModel.descricao
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = descricao, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            CatalogoPaginaListView(onItemClick)
        }
    }

    @Composable
    fun CatalogoPaginaListView(onItemClick: (registro: CatalogoPagina) -> Unit) {
        val registros = viewModel.registros.collectAsState()
        LazyColumn(
            contentPadding = PaddingValues(5.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            itemsIndexed(items = registros.value) { _, value ->
                CatalogoPaginaListViewItem(value, onItemClick)
            }
        }
    }

    @Composable
    fun CatalogoPaginaListViewItem(
        registro: CatalogoPagina,
        onItemClick: (registro: CatalogoPagina) -> Unit
    ) {
        val identificador = viewModel.identificador
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onItemClick(registro)
                },
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("${ApplicationLocate.instance.dotenv[Constantes.IMAGEKIT_ENDPOINT]}/catalogo/catalogos/${identificador}/paginas/${registro.name}")
                        .crossfade(true)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .diskCacheKey("${identificador}-${registro.name}")
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .build(),
                    contentDescription = stringResource(R.string.app_name),
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .padding(20.dp),
                )
                Text(
                    text = "${registro.pagina}",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End
                )
            }
        }
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    fun openDialog(fm: FragmentManager) {
        if (fm.findFragmentByTag(DIALOG_TAG) == null) {
            show(fm, DIALOG_TAG)
        }
    }
 
    private fun showMenu(
        v: View,
        @MenuRes menuRes: Int,
        catalogoId: Long,
        catalogoIdPagina: Long
    ) {
        val popup = PopupMenu(requireContext(), v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            popup.setForceShowIcon(true)
        }

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            return@setOnMenuItemClickListener when (menuItem.itemId) {

                R.id.menu_catalogo_pagina_pd_catalogo -> {
                    UtilCatalogo.listarProdutosDoCatalogo(catalogoId)
                    true
                }

                R.id.menu_catalogo_pagina_pd_pagina -> {
                    UtilCatalogo.listarProdutosDoCatalogo(catalogoId, catalogoIdPagina)
                    true
                }

                else -> false
            }
        }
        popup.show()
    }
}
