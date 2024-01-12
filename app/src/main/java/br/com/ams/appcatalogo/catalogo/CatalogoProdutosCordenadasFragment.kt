package br.com.ams.appcatalogo.catalogo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import br.com.ams.appcatalogo.R
import br.com.ams.appcatalogo.common.ValorRealUtil
import br.com.ams.appcatalogo.entity.Produto
import br.com.ams.appcatalogo.viewsmodel.CatalogoProdutosCordenadasViewModel
import com.blankj.utilcode.util.ToastUtils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class CatalogoProdutosCordenadasFragment : BottomSheetDialogFragment() {

    private val viewModel: CatalogoProdutosCordenadasViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme {
                    CatalogoProdutosCordenadasListView()
                }
            }
        }
    }

    @Composable
    fun CatalogoProdutosCordenadasListView() {
        val registros = viewModel.registros.collectAsState()

        LaunchedEffect(key1 = Unit) {
            viewModel.carregarRegistros(arguments?.getLongArray(EXTRA_PRODUTOS))
        }

        LazyColumn(
            contentPadding = PaddingValues(5.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            itemsIndexed(items = registros.value) { _, value ->
                CatalogoProdutosCordenadasListViewItem(value)
                Divider(color = MaterialTheme.colorScheme.primary, thickness = 1.dp)
            }
        }
    }

    @Composable
    fun CatalogoProdutosCordenadasListViewItem(registro: Produto) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                Modifier
                    .weight(1F)
                    .fillMaxWidth()) {
                Text(
                    text = registro.descricao!!,
                )
                Text(
                    text = ValorRealUtil.formatarValorReal(registro.valor!!),
                    modifier = Modifier
                        .padding(PaddingValues(end = 10.dp)),
                    textAlign = TextAlign.End,
                )
            }
            ButtonLancarProdutoPedidoView()
        }
    }

    @Composable
    fun ButtonLancarProdutoPedidoView() {
        IconButton(onClick = { ToastUtils.showLong(R.string.nao_implementado) }) {
            Icon(
                Icons.Filled.ShoppingCart,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = stringResource(R.string.descricao_lancar_produto_pedido)
            )
        }
    }

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = LinearLayout.LayoutParams.MATCH_PARENT
        params.height = LinearLayout.LayoutParams.MATCH_PARENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
    }

    fun openDialog(fm: FragmentManager) {
        if (fm.findFragmentByTag(DIALOG_TAG) == null) {
            show(fm, DIALOG_TAG)
        }
    }

    companion object {
        val EXTRA_PRODUTOS = "produtos"
        private const val DIALOG_TAG = "produtosCorndenadasDialog"

        @JvmStatic
        fun newInstance(produtos: LongArray): CatalogoProdutosCordenadasFragment {
            val fragment = CatalogoProdutosCordenadasFragment().apply {
                arguments = Bundle().apply {
                    putLongArray(EXTRA_PRODUTOS, produtos)
                }
            }
            return fragment
        }
    }
}