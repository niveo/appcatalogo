package br.com.ams.appcatalogo.produto

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import br.com.ams.appcatalogo.ApplicationLocate
import br.com.ams.appcatalogo.R
import br.com.ams.appcatalogo.common.Constantes
import br.com.ams.appcatalogo.common.Funcoes
import br.com.ams.appcatalogo.common.TaskObserver
import br.com.ams.appcatalogo.databinding.FragmentProdutoListaBinding
import br.com.ams.appcatalogo.produto.dataadapter.ProdutoListaDataAdapter
import br.com.ams.appcatalogo.repository.ProdutoRepository
import javax.inject.Inject

class ProdutoListaFragment : DialogFragment() {
    private lateinit var binding: FragmentProdutoListaBinding
    private lateinit var adapter: ProdutoListaDataAdapter

    @Inject
    lateinit var produtoRepository: ProdutoRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApplicationLocate.component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProdutoListaBinding
            .inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ProdutoListaDataAdapter()

        Funcoes.configurarRecyclerDefault(
            requireContext(),
            binding.fragmentProdutoListaRecycler,
            adapter,
            false
        )

        arguments?.let {
            carregarRegistros(it)
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

    companion object {
        private const val DIALOG_TAG = "ProdutoListaFragment"

        @JvmStatic
        fun newInstance(bundle: Bundle?) =
            ProdutoListaFragment().apply {
                arguments = bundle
            }
    }

    private fun carregarRegistros(bundle: Bundle?) {
        binding.fragmentProdutoListaRegistros.text = "";
        TaskObserver.runInSingle(requireContext(), {
            if (bundle == null) {
                produtoRepository.getAll()
            } else if (bundle.containsKey(Constantes.CATALOGO_ID) && bundle.containsKey(Constantes.CATALOGO_PAGINA_ID)) {
                produtoRepository.obterProdutoCatalogoPagina(
                    bundle.getLong(Constantes.CATALOGO_ID),
                    bundle.getLong(Constantes.CATALOGO_PAGINA_ID)
                )
            } else if (bundle.containsKey(Constantes.CATALOGO_ID)) {
                produtoRepository.obterProdutoCatalogo(bundle.getLong(Constantes.CATALOGO_ID))
            } else {
                produtoRepository.getAll()
            }


        }, {
            binding.fragmentProdutoListaRegistros.text = "${it?.size}";
            adapter.carregarRegistros(it)
        }, {
            Funcoes.alertaThrowable(it)
        }, true)
    }
}