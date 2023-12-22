package br.com.ams.appcatalogo.catalogo


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
import br.com.ams.appcatalogo.common.Funcoes
import br.com.ams.appcatalogo.common.TaskObserver
import br.com.ams.appcatalogo.database.AppDatabase
import br.com.ams.appcatalogo.databinding.ActivityVisualizaImagemBinding
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.davemorrissey.labs.subscaleview.ImageSource
import org.greenrobot.eventbus.EventBus
import java.io.File

class VisualizaImagemActivity : AppCompatActivity() {

    lateinit var binding: ActivityVisualizaImagemBinding
    var codigoCatalogoPagina: Long? = null
    var codigoCatalogo: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVisualizaImagemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val extras = intent.extras

        binding.btnSair.setOnClickListener {
            finish()
        }

        codigoCatalogo = extras!!.getLong("KEY_CODIGO_CATALOGO")

        codigoCatalogoPagina = extras.getLong("KEY_CODIGO_CATALOGO_PAGINA")

        val localFile = extras.getString("KEY_LOCAL_FILE_CATALOGO", "")

        val fileCatalogo = File(localFile)

        if (!fileCatalogo.exists()) {
            ToastUtils.showLong(
                getString(R.string.arquivo_nao_localizado)
            )
        } else {

            binding.activityVisualizaImagemImg.setImage(
                ImageSource.bitmap(
                    ImageUtils.getBitmap(fileCatalogo)
                )
            )

            val gestureDetector =
                GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
                    override fun onSingleTapConfirmed(e: MotionEvent): Boolean {

                        if (binding.activityVisualizaImagemImg.isReady) {

                            val sCoord: PointF =
                                binding.activityVisualizaImagemImg.viewToSourceCoord(e.x, e.y)!!

                            carregarRegistros(
                                codigoCatalogo!!,
                                codigoCatalogoPagina!!,
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
        }
    }

    private fun carregarRegistros(
        codigoCatalogo: Long,
        codigoCatalogoPagina: Long,
        sCoord: PointF
    ) {
        /*TaskObserver.runInSingle(this, {
            ApplicationLocate.getInstance().dataBase
                .catalogoPaginaProdutoDao()
                .obterIdProduto( codigoCatalogo,
                    codigoCatalogoPagina,
                    sCoord.x,
                    sCoord.y)
        }, {
            if (!it.isNullOrEmpty()) {
                val newInstance =
                    ProdutosCordenadasFragmentDialog.newInstance(it.toLongArray())
                newInstance.openDialog(supportFragmentManager)
            } else {
                ToastUtils.showLong(
                    getString(R.string.produto_nao_localizado_cordenada)
                )
            }
        }, {
            Funcoes.alertaThrowable(it)
        }, true)*/
    }
}