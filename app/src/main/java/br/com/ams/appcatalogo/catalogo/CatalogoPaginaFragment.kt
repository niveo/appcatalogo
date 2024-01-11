package br.com.ams.appcatalogo.catalogo

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import br.com.ams.appcatalogo.R
import br.com.ams.appcatalogo.catalogo.dataadapter.CatalogoPaginaDataAdapter
import br.com.ams.appcatalogo.catalogo.utils.UtilCatalogo
import br.com.ams.appcatalogo.common.Constantes
import br.com.ams.appcatalogo.common.Funcoes
import br.com.ams.appcatalogo.databinding.FragmentCatalogoPaginaBinding
import br.com.ams.appcatalogo.viewsmodel.CatalogoPaginaViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

private const val PARAM_CODIGO: String = "PARAM_CODIGO"
private const val PARAM_DESCRICAO: String = "PARAM_DESCRICAO"
private const val PARAM_IDENTIFICADOR: String = "PARAM_IDENTIFICADOR"

class CatalogoPaginaFragment : DialogFragment() {
    private lateinit var binding: FragmentCatalogoPaginaBinding
    private var codigo: Long? = null
    private var descricao: String? = null
    private var identificador: String? = null
    private lateinit var dataAdapter: CatalogoPaginaDataAdapter

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
            codigo = it.getLong(PARAM_CODIGO)
            identificador = it.getString(PARAM_IDENTIFICADOR)
            descricao = it.getString(PARAM_DESCRICAO, "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCatalogoPaginaBinding
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSair.setOnClickListener {
            dismiss()
        }

        binding.txtDescricao.text = this.descricao

        dataAdapter =
            CatalogoPaginaDataAdapter(
                this.identificador!!,
                object : CatalogoPaginaDataAdapter.OnItemTouchListener {
                    override fun onDetalhar(view: View, position: Int) {
                        val registro = dataAdapter.obterRegistro(position)
                        val intent = Intent(
                            this@CatalogoPaginaFragment.context,
                            CatalogoImagemActivity::class.java
                        )
                        intent.putExtra(Constantes.KEY_CATALOGO_IDENTIFICADOR, identificador)
                        intent.putExtra(Constantes.KEY_CATALOGO_CODIGO, registro.catalogoId)
                        intent.putExtra(Constantes.KEY_CATALOGO_PAGINA_CODIGO, registro.id)
                        intent.putExtra(Constantes.KEY_CATALOGO_NOME, registro.name)
                        startActivity(intent)
                    }

                    override fun onMenu(v: View, position: Int) {
                        val registro = dataAdapter.obterRegistro(position)
                        showMenu(
                            v,
                            R.menu.menu_catalogo_pagina,
                            registro.catalogoId!!,
                            registro.id
                        )
                    }
                })

        Funcoes.configurarRecyclerDefault(
            requireContext(),
            binding.activityCatalogopaginaRecycler,
            dataAdapter,
            false
        )

        viewModel.carregarCatalogoPaginaMapeados(this.codigo!!)
        viewModel.registros.onEach {
            dataAdapter.carregarRegistros(it)
        }.launchIn(lifecycleScope)
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
