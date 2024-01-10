package br.com.ams.appcatalogo.catalogo


import android.annotation.SuppressLint
import android.graphics.PointF
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.room.Room
import br.com.ams.appcatalogo.ApplicationLocate
import br.com.ams.appcatalogo.R
import br.com.ams.appcatalogo.common.Constantes
import br.com.ams.appcatalogo.common.Funcoes
import br.com.ams.appcatalogo.common.TaskObserver
import br.com.ams.appcatalogo.database.AppDatabase
import br.com.ams.appcatalogo.databinding.ActivityVisualizaImagemBinding
import br.com.ams.appcatalogo.repository.ProdutoRepository
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.davemorrissey.labs.subscaleview.ImageSource
import com.example.imagekit.android.picasso_extension.createWithPicasso
import com.imagekit.android.ImageKit
import com.imagekit.android.entity.TransformationPosition
import org.greenrobot.eventbus.EventBus
import java.io.File
import javax.inject.Inject

class VisualizaImagemActivity : AppCompatActivity() {

    lateinit var binding: ActivityVisualizaImagemBinding

    @Inject
    lateinit var produtoRepository: ProdutoRepository

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVisualizaImagemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ApplicationLocate.component.inject(this)

        val extras = intent.extras

        binding.btnSair.setOnClickListener {
            finish()
        }

        val codigoCatalogo = extras!!.getLong(Constantes.KEY_CATALOGO_CODIGO)
        val codigoCatalogoPagina = extras.getLong(Constantes.KEY_CATALOGO_PAGINA_CODIGO)

        val gestureDetector =
            GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapConfirmed(e: MotionEvent): Boolean {

                    if (binding.activityVisualizaImagemImg.isReady) {

                        val sCoord: PointF =
                            binding.activityVisualizaImagemImg.viewToSourceCoord(e.x, e.y)!!

                        carregarRegistros(
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

        carregarImagem(
            extras.getString(Constantes.KEY_CATALOGO_NOME)!!,
            extras.getString(Constantes.KEY_CATALOGO_IDENTIFICADOR)!!
        )
    }

    private fun carregarImagem(nome: String, identificador: String) {
        TaskObserver.runInSingle(this, {
            ImageKit.getInstance()
                .url(
                    path = nome,
                    transformationPosition = TransformationPosition.QUERY,
                    urlEndpoint = "${ApplicationLocate.instance.dotenv[Constantes.IMAGEKIT_ENDPOINT]}/catalogo/catalogos/${identificador}/paginas/"
                )
                .createWithPicasso()
                .get()
        }, {
            binding.activityVisualizaImagemImg.setImage(
                ImageSource.bitmap(it!!)
            )
        }, {
            Funcoes.alertaThrowable(it)
        }, true)
    }

    private fun carregarRegistros(
        codigoCatalogo: Long,
        codigoCatalogoPagina: Long,
        sCoord: PointF
    ) {
        TaskObserver.runInSingle(this, {
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

            registros
        }, {
            if (!it.isNullOrEmpty()) {
                val newInstance =
                    CatalogoProdutosCordenadasFragment.newInstance(it.toLongArray())
                newInstance.openDialog(supportFragmentManager)
            } else {
                ToastUtils.showLong(
                    getString(R.string.produto_nao_localizado_cordenada)
                )
            }
        }, {
            Funcoes.alertaThrowable(it)
        }, true)
    }
}