package br.com.ams.appcatalogo.produto

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import br.com.ams.appcatalogo.R
import br.com.ams.appcatalogo.catalogo.CatalogoPaginaFragment
import br.com.ams.appcatalogo.common.Funcoes
import br.com.ams.appcatalogo.common.TaskObserver
import br.com.ams.appcatalogo.databinding.FragmentProdutoListaBinding
import br.com.ams.appcatalogo.repository.ProdutoRepository
import com.blankj.utilcode.util.LogUtils
import javax.inject.Inject

class ProdutoListaFragment : DialogFragment() {
    private lateinit var binding: FragmentProdutoListaBinding

    @Inject
    private lateinit var produtoRepository: ProdutoRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            carregarRegistros()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProdutoListaBinding
            .inflate(inflater, container, false)
        return binding.root
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

    private fun carregarRegistros() {
        TaskObserver.runInSingle(requireContext(), {
            //produtoRepository.obterProdutoCatalogo()
        }, {

        }, {
            Funcoes.alertaThrowable(it)
        }, true)
    }
}