package br.com.ams.appcatalogo.catalogo

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.work.WorkManager
import br.com.ams.appcatalogo.ApplicationLocate
import br.com.ams.appcatalogo.R
import br.com.ams.appcatalogo.catalogo.dataadapter.CatalogoDataAdapter
import br.com.ams.appcatalogo.common.*
import br.com.ams.appcatalogo.databinding.ActivityCatalogoBinding
import br.com.ams.appcatalogo.model.bus.MessageBusIdentificador
import br.com.ams.appcatalogo.produto.ProdutoListaFragment
import br.com.ams.appcatalogo.repository.CatalogoRepository
import br.com.ams.appcatalogo.service.AtualizarDadosServiceWorker
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject

class CatalogoActivity : AppCompatActivity() {
    private var dialog: ProgressDialogUtil? = null
    private lateinit var cardViewCatalogoAdapter: CatalogoDataAdapter
    private lateinit var binding: ActivityCatalogoBinding

    @Inject
    lateinit var catalogoRepository: CatalogoRepository

    var account: Auth0 = Auth0(
        ApplicationLocate.instance.dotenv[Constantes.COM_AUTH0_CLIENT_ID],
        ApplicationLocate.instance.dotenv[Constantes.COM_AUTH0_DOMAIN],
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCatalogoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        EventBus.getDefault().register(this)

        dialog = ProgressDialogUtil(this)

        ApplicationLocate.component.inject(this)

        cardViewCatalogoAdapter = CatalogoDataAdapter(
            object : CatalogoDataAdapter.OnItemTouchListener {
                override fun onDetalhar(view: View, position: Int) {

                    val catalogoSelecionado = cardViewCatalogoAdapter.getItem(position)

                    CatalogoPaginaFragment.newInstance(
                        catalogoSelecionado.id,
                        catalogoSelecionado.descricao,
                        catalogoSelecionado.identificador!!
                    ).openDialog(supportFragmentManager)

                }
            })

        Funcoes.configurarRecyclerDefault(
            this,
            binding.activityCatalogoRecycler,
            cardViewCatalogoAdapter,
            false
        )

        carregarRegistros()
    }

    private fun carregarRegistros() {
        TaskObserver.runInSingle(this, {
            catalogoRepository.getAll()
        }, {
            cardViewCatalogoAdapter.carregarRegistros(it)
        }, {
            Funcoes.alertaThrowable(it)
        }, true)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEventMainCentralActivity(event: MessageBusIdentificador) {
        when (event.identificador) {
            Constantes.NT_CONSULTA_PRODUTO -> {
                ProdutoListaFragment.newInstance(event.bundle)
                    .openDialog(supportFragmentManager)
            }

            else -> {
                LogUtils.w("Identificador ${event.identificador} não localizado.")
                //ToastUtils.showLong("Identificador ${event.identificador} não localizado.")
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_central, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_central_atualizar_registros -> {
                atualizarRegistros()
                true
            }

            R.id.menu_central_logout -> {
                logout()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun atualizarRegistros() {
        val id = AtualizarDadosServiceWorker.iniciar(this)
        val mWorkManager = WorkManager.getInstance(this)
        mWorkManager.getWorkInfoByIdLiveData(id)
            .observe(this, Observer {
                if (it.state.isFinished) {
                    carregarRegistros()
                    ToastUtils.showLong(R.string.registros_atualizados)
                }
            })
    }

    private fun logout() {
        WebAuthProvider.logout(account)
            .withScheme("demo")
            .start(this, object : Callback<Void?, AuthenticationException> {
                override fun onSuccess(payload: Void?) {
                    val sp = SPUtils.getInstance()
                    sp.remove(Constantes.KEY_TOKEN_BEARER, true)
                    sp.remove(Constantes.KEY_TOKEN_USER_ID, true)

                    AppUtils.exitApp()
                }

                override fun onFailure(error: AuthenticationException) {
                    LogUtils.e(error)
                    ToastUtils.showLong(R.string.erro_processo)
                }
            })
    }
}
