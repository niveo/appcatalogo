package br.com.ams.appcatalogo.catalogo

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import br.com.ams.appcatalogo.ApplicationLocate
import br.com.ams.appcatalogo.R
import br.com.ams.appcatalogo.catalogo.utils.CatalogoServiceWorker
import br.com.ams.appcatalogo.catalogo.utils.MensagemBusCatalogo
import br.com.ams.appcatalogo.common.*
import br.com.ams.appcatalogo.databinding.ActivityCatalogoBinding
import br.com.ams.appcatalogo.enuns.TipoEnvioEmail
import br.com.ams.appcatalogo.fragments.EnviarCopiaEmailDialogFragment
import br.com.ams.appcatalogo.service.AtualizarDadosLocalService
import br.com.ams.appcatalogo.view.ConfiguracaoActivity
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File

class CatalogoActivity : AppCompatActivity() {
    private var dialog: ProgressDialogUtil? = null
    private var catalogoSelecionadoPosicao: Int? = null
    private var cardViewCatalogoAdapter: CardViewCatalogoAdapter? = null
    private lateinit var binding: ActivityCatalogoBinding
    private var carregandoWorkCatalogo = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCatalogoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        EventBus.getDefault().register(this)

        dialog = ProgressDialogUtil(this)

        cardViewCatalogoAdapter = CardViewCatalogoAdapter(
            object : CardViewCatalogoAdapter.OnItemTouchListener {
                override fun onDetalhar(view: View, position: Int) {

                    if (!carregandoWorkCatalogo) {
                        catalogoSelecionadoPosicao = position
                        val catalogoSelecionado = cardViewCatalogoAdapter!!.getItem(position)!!

                        CatalogoServiceWorker.iniciar(
                            this@CatalogoActivity,
                            catalogoSelecionado.codigo!!
                        )

                    } else {
                        ToastUtils.showLong(getString(R.string.catalogo_sendo_iniciado))
                    }
                }

                override fun onMenu(view: View, position: Int) {
                    showMenu(view, R.menu.menu_catalogo, position)
                }
            })

        Funcoes.configurarRecyclerDefault(
            this,
            binding.activityCatalogoRecycler,
            cardViewCatalogoAdapter!!,
            false
        )

        binding.btnBaixarCatalogos.setOnClickListener {
            onAtualizarRegistros()
        }
        carregarRegistros()
    }

    fun onAtualizarRegistros() {
        dialog!!.show()
        AtualizarDadosLocalService().iniciar(this, {
            carregarRegistros()
            dialog!!.dismiss()
        }, {
            ToastUtils.showLong(it.message)
            dialog!!.dismiss()
        })
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMensagemBusCatalogoActivity(msg: MensagemBusCatalogo) {
        if (msg.iniciado) {
            carregandoWorkCatalogo = true
            cardViewCatalogoAdapter?.progressRegistro(true, catalogoSelecionadoPosicao!!)
        } else {
            carregandoWorkCatalogo = false
            cardViewCatalogoAdapter?.progressRegistro(false, catalogoSelecionadoPosicao!!)
            if (msg.valido) {
                val catalogoSelecionado =
                    cardViewCatalogoAdapter!!.getItem(catalogoSelecionadoPosicao!!)!!
                CatalogoPaginaFragment.newInstance(
                    catalogoSelecionado.codigo!!,
                    catalogoSelecionado.descricao!!
                ).openDialog(supportFragmentManager)
            } else {
                ToastUtils.showLong("Erro baixar catalogo.")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    private fun showMenu(v: View, @MenuRes menuRes: Int, position: Int) {

        catalogoSelecionadoPosicao = position
        val catalogoSelecionado = cardViewCatalogoAdapter!!.getItem(position)!!

        val popup = PopupMenu(this, v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            popup.setForceShowIcon(true)
        }

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            return@setOnMenuItemClickListener when (menuItem.itemId) {
                R.id.menu_catalogo_atualizar -> {
                    removerCatalogoDiretorio(catalogoSelecionado.codigo!!)
                    true
                }
                R.id.menu_catalogo_email -> {
                    enviarCatalogoEmail(catalogoSelecionado.codigo!!)
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    private fun enviarCatalogoEmail(codigoCatalogo: Long) {
        EnviarCopiaEmailDialogFragment.newInstance(
            TipoEnvioEmail.CATALOGO,
            codigoCatalogo.toString(),
            null
        ).openDialog(supportFragmentManager)
    }

    private fun removerCatalogoDiretorio(codigoCatalogo: Long) {
        DialogsUtils.showConfirmacao(
            this,
            getString(R.string.alerta),
            getString(R.string.remover_catalogo_atualizar), {
                if (it) {
                    if (FileUtils.delete(File(Config.PATH_AMS_CATALOGO + Config.FILE_SEP + codigoCatalogo))) {
                        CatalogoServiceWorker.iniciar(this, codigoCatalogo)
                    } else {
                        ToastUtils.showLong(getString(R.string.erro_excluir_diretorio))
                    }
                }
            }
        )
    }

    private fun carregarRegistros() {
        TaskObserver.runInSingle(this, {
            ApplicationLocate.instance.dataBase.catalogoDao()
                .obterCatalogoMapeados()
        }, {
            if (it.isNullOrEmpty()) {
                binding.btnBaixarCatalogos.visibility = View.VISIBLE
                binding.activityCatalogoRecycler.visibility = View.GONE
            } else {
                binding.btnBaixarCatalogos.visibility = View.GONE
                binding.activityCatalogoRecycler.visibility = View.VISIBLE
            }
            cardViewCatalogoAdapter!!.carregarRegistros(it)
        }, {
            Funcoes.alertaThrowable(it)
        }, true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_central, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_central_atualizar_registros -> {
                onAtualizarRegistros()
                return true
            }
            R.id.menu_central_atualizar_configuracao -> {
                ActivityUtils.startActivity(ConfiguracaoActivity::class.java)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
