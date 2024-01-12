package br.com.ams.appcatalogo.catalogo

import android.annotation.SuppressLint
import android.graphics.PointF
import android.os.Bundle
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import br.com.ams.appcatalogo.R
import br.com.ams.appcatalogo.common.Constantes
import br.com.ams.appcatalogo.databinding.FragmentCatalogoImagemBinding
import br.com.ams.appcatalogo.viewsmodel.CatalogoImagemViewModel
import com.blankj.utilcode.util.ToastUtils
import com.davemorrissey.labs.subscaleview.ImageSource
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.properties.Delegates

class CatalogoImagemFragment : DialogFragment() {
    private lateinit var binding: FragmentCatalogoImagemBinding

    private val viewModel: CatalogoImagemViewModel by activityViewModels()

    private var codigoCatalogo by Delegates.notNull<Long>()
    private var codigoCatalogoPagina by Delegates.notNull<Long>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {

            codigoCatalogo = it.getLong(Constantes.KEY_CATALOGO_CODIGO)
            codigoCatalogoPagina = it.getLong(Constantes.KEY_CATALOGO_PAGINA_CODIGO)

            viewModel.carregarImagem(
                it.getString(Constantes.KEY_CATALOGO_NOME)!!,
                it.getString(Constantes.KEY_CATALOGO_IDENTIFICADOR)!!
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCatalogoImagemBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gestureDetector =
            GestureDetector(requireContext(), object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                    if (binding.activityVisualizaImagemImg.isReady) {
                        val sCoord: PointF =
                            binding.activityVisualizaImagemImg.viewToSourceCoord(e.x, e.y)!!
                        viewModel.carregarRegistrosCordenadas(
                            codigoCatalogo,
                            codigoCatalogoPagina,
                            sCoord
                        )
                    }
                    return true
                }

            })

        binding.activityVisualizaImagemImg.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(
                event
            )
        }

       viewModel.imagem.onEach {
           if (it != null) {
               binding.activityVisualizaImagemImg.setImage(
                   ImageSource.bitmap(it)
               )
           }
       }.launchIn(lifecycleScope)

        viewModel.registrosCordenadas.onEach {
            if (it.isNotEmpty()) {
                val newInstance =
                    CatalogoProdutosCordenadasFragment.newInstance(it)
                newInstance.openDialog(parentFragmentManager)
            } else {
                ToastUtils.showLong(
                    getString(R.string.produto_nao_localizado_cordenada)
                )
            }
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
        private const val DIALOG_TAG = "CatalogoImagemFragment"

        @JvmStatic
        fun newInstance(
            identificador: String,
            nome: String,
            catalogoId: Long,
            catalogoPaginaId: Long
        ) =
            CatalogoImagemFragment().apply {
                arguments = Bundle().apply {
                    putString(Constantes.KEY_CATALOGO_IDENTIFICADOR, identificador)
                    putLong(Constantes.KEY_CATALOGO_CODIGO, catalogoId)
                    putLong(Constantes.KEY_CATALOGO_PAGINA_CODIGO, catalogoPaginaId)
                    putString(Constantes.KEY_CATALOGO_NOME, nome)
                }
            }
    }
}