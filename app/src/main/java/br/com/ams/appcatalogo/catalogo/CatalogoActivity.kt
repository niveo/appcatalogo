package br.com.ams.appcatalogo.catalogo

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.appcompat.app.AppCompatActivity
import br.com.ams.appcatalogo.ApplicationLocate
import br.com.ams.appcatalogo.R
import br.com.ams.appcatalogo.catalogo.utils.MensagemBusCatalogo
import br.com.ams.appcatalogo.common.*
import br.com.ams.appcatalogo.databinding.ActivityCatalogoBinding
import br.com.ams.appcatalogo.view.ConfiguracaoActivity
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ToastUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class CatalogoActivity : AppCompatActivity() {
    private var dialog: ProgressDialogUtil? = null
    private var catalogoSelecionadoPosicao: Int? = null
    private var cardViewCatalogoAdapter: CardViewCatalogoAdapter? = null
    private lateinit var binding: ActivityCatalogoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCatalogoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        EventBus.getDefault().register(this)

        dialog = ProgressDialogUtil(this)

        cardViewCatalogoAdapter = CardViewCatalogoAdapter(
            object : CardViewCatalogoAdapter.OnItemTouchListener {
                override fun onDetalhar(view: View, position: Int) {

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

        carregarRegistros()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMensagemBusCatalogoActivity(msg: MensagemBusCatalogo) {
        val catalogoSelecionado =
            cardViewCatalogoAdapter!!.getItem(catalogoSelecionadoPosicao!!)!!
        CatalogoPaginaFragment.newInstance(
            catalogoSelecionado.id!!,
            catalogoSelecionado.descricao!!
        ).openDialog(supportFragmentManager)
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

                    true
                }
                R.id.menu_catalogo_email -> {

                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    private fun carregarRegistros() {
        TaskObserver.runInSingle(this, {
            ApplicationLocate.instance.dataBase!!.catalogoDao()
                .carregarCatalogos()
        }, {
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
