package br.com.ams.appcatalogo.produto

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import br.com.ams.appcatalogo.R
import br.com.ams.appcatalogo.common.Funcoes
import br.com.ams.appcatalogo.databinding.FragmentProdutoListaBinding
import br.com.ams.appcatalogo.produto.dataadapter.ProdutoListaDataAdapter
import br.com.ams.appcatalogo.viewsmodel.ProdutoViewModel
import com.blankj.utilcode.util.LogUtils
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ProdutoListaFragment : DialogFragment() {
    private lateinit var binding: FragmentProdutoListaBinding
    private lateinit var adapter: ProdutoListaDataAdapter

    private val viewModel: ProdutoViewModel by activityViewModels()

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
            viewModel.carregarRegistros(it)
        }

        viewModel.registros.onEach {
            LogUtils.w(it)
            binding.fragmentProdutoListaRegistros.text = "${it.size}";
            adapter.carregarRegistros(it)
        }.launchIn(lifecycleScope)
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

}