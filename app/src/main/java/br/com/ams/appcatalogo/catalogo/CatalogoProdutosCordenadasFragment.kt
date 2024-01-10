package br.com.ams.appcatalogo.catalogo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.fragment.app.FragmentManager
import br.com.ams.appcatalogo.R
import br.com.ams.appcatalogo.catalogo.dataadapter.CatalogoProdutosCordenadasDataAdapter
import br.com.ams.appcatalogo.common.Funcoes
import br.com.ams.appcatalogo.databinding.CatalogoProdutosCordenadasFragmentBinding
import com.blankj.utilcode.util.ToastUtils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CatalogoProdutosCordenadasFragment : BottomSheetDialogFragment() {

    private var cardViewProdutosCordenadas: CatalogoProdutosCordenadasDataAdapter? = null
    private lateinit var binding: CatalogoProdutosCordenadasFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CatalogoProdutosCordenadasFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val longArray = arguments?.getLongArray(EXTRA_PRODUTOS)

        cardViewProdutosCordenadas = CatalogoProdutosCordenadasDataAdapter(
            object : CatalogoProdutosCordenadasDataAdapter.OnItemTouchListener {
                override fun onDetalhar(view: View, position: Int) {
                    ToastUtils.showLong(getString(R.string.nao_implementado))
                    dismiss()
                }

                override fun onPedido(view: View, position: Int) {
                    ToastUtils.showLong(getString(R.string.nao_implementado))
                }
            })

        Funcoes.configurarRecyclerDefault(
            view.context,
            binding.produtosCordenadasFragmentdialogRecycler,
            cardViewProdutosCordenadas!!,
            true
        )

        carregarTransacao(longArray!!)

    }

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = LinearLayout.LayoutParams.MATCH_PARENT
        params.height = LinearLayout.LayoutParams.MATCH_PARENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
    }

    fun carregarTransacao(produtos: LongArray) {
        /*TaskObserver.runInSingle(requireContext(), {
            ApplicationLocate.instance.dataBase!!.produtoDao()
                .obterProdutoCodigos(produtos.asList())
        }, {
            if (it.) {
                ToastUtils.showLong(getString(R.string.registros_nao_localizados))
                dismiss()
            } else {
                cardViewProdutosCordenadas!!.carregarRegistros(it)
            }
        }, {
            Funcoes.alertaThrowable(it)
        }, true)*/
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