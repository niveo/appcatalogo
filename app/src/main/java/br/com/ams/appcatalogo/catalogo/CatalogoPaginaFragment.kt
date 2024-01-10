package br.com.ams.appcatalogo.catalogo

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
import br.com.ams.appcatalogo.ApplicationLocate
import br.com.ams.appcatalogo.R
import br.com.ams.appcatalogo.catalogo.dataadapter.CatalogoPaginaDataAdapter
import br.com.ams.appcatalogo.catalogo.utils.UtilCatalogo
import br.com.ams.appcatalogo.common.Funcoes
import br.com.ams.appcatalogo.common.TaskObserver
import br.com.ams.appcatalogo.databinding.FragmentCatalogoPaginaBinding
import br.com.ams.appcatalogo.repository.CatalogoPaginaRepository
import javax.inject.Inject

private const val PARAM_CODIGO: String = "PARAM_CODIGO"
private const val PARAM_DESCRICAO: String = "PARAM_DESCRICAO"
private const val PARAM_IDENTIFICADOR: String = "PARAM_IDENTIFICADOR"

class CatalogoPaginaFragment : DialogFragment() {
    private lateinit var binding: FragmentCatalogoPaginaBinding
    private var codigo: Long? = null
    private var identificador: String? = null
    private var descricao: String? = null
    private var cardViewCatalogoAdapter: CatalogoPaginaDataAdapter? = null

    @Inject
    lateinit var catalogoPaginaRepository: CatalogoPaginaRepository

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
        ApplicationLocate.component.inject(this)
        arguments?.let {
            descricao = it.getString(PARAM_DESCRICAO, "")
            codigo = it.getLong(PARAM_CODIGO)
            identificador = it.getString(PARAM_IDENTIFICADOR)
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

        this.carregarCatalogo()

        cardViewCatalogoAdapter =
            CatalogoPaginaDataAdapter(
                requireContext(), this.identificador!!,
                object : CatalogoPaginaDataAdapter.OnItemTouchListener {
                    override fun onDetalhar(view: View, position: Int) {
                        /*val registro = cardViewCatalogoAdapter!!.obterRegistro(position)
                        val fileCatalogo = UtilCatalogo.arquivoCatalogo(registro)
                        val intent = Intent(
                            this@CatalogoPaginaFragment.context,
                            VisualizaImagemActivity::class.java
                        )
                        intent.putExtra("KEY_LOCAL_FILE_CATALOGO", fileCatalogo.toString())
                        intent.putExtra("KEY_CODIGO_CATALOGO", registro.catalogoId)
                        intent.putExtra("KEY_CODIGO_CATALOGO_PAGINA", registro.id)
                        startActivity(intent)*/
                    }

                    override fun onMenu(v: View?, absoluteAdapterPosition: Int) {
                        val registro =
                            cardViewCatalogoAdapter!!.obterRegistro(absoluteAdapterPosition)
                        showMenu(
                            v!!,
                            R.menu.menu_catalogo_pagina,
                            registro.catalogoId!!,
                            registro.id
                        )
                    }
                })

        Funcoes.configurarRecyclerDefault(
            requireContext(),
            binding.activityCatalogopaginaRecycler,
            cardViewCatalogoAdapter!!,
            false
        )
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

    fun carregarCatalogo() {
        binding.txtDescricao.text = this.descricao
        carregarRegistros()
    }

    private fun carregarRegistros() {
        TaskObserver.runInSingle(requireContext(), {
            catalogoPaginaRepository.obterCatalogoPaginaMapeados(this.codigo!!)
        }, {
            cardViewCatalogoAdapter!!.carregarRegistros(it)
        }, {
            Funcoes.alertaThrowable(it)
        }, true)
    }
}
