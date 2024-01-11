package br.com.ams.appcatalogo.catalogo


import android.annotation.SuppressLint
import android.graphics.PointF
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import br.com.ams.appcatalogo.ApplicationLocate
import br.com.ams.appcatalogo.R
import br.com.ams.appcatalogo.common.Constantes
import br.com.ams.appcatalogo.common.Funcoes
import br.com.ams.appcatalogo.common.TaskObserver
import br.com.ams.appcatalogo.databinding.ActivityCatalogoImagemBinding
import br.com.ams.appcatalogo.repository.ProdutoRepository
import com.blankj.utilcode.util.ToastUtils
import com.davemorrissey.labs.subscaleview.ImageSource
import com.example.imagekit.android.picasso_extension.createWithPicasso
import com.imagekit.android.ImageKit
import com.imagekit.android.entity.TransformationPosition
import javax.inject.Inject

class CatalogoImagemActivity : AppCompatActivity() {

    lateinit var binding: ActivityCatalogoImagemBinding

    @Inject
    lateinit var produtoRepository: ProdutoRepository

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCatalogoImagemBinding.inflate(layoutInflater)
        setContentView(binding.root)

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