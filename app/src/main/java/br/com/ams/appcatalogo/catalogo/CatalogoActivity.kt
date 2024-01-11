package br.com.ams.appcatalogo.catalogo

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import br.com.ams.appcatalogo.ApplicationLocate
import br.com.ams.appcatalogo.R
import br.com.ams.appcatalogo.catalogo.dataadapter.CatalogoDataAdapter
import br.com.ams.appcatalogo.common.*
import br.com.ams.appcatalogo.databinding.ActivityCatalogoBinding
import br.com.ams.appcatalogo.model.bus.MessageBusIdentificador
import br.com.ams.appcatalogo.produto.ProdutoListaFragment
import br.com.ams.appcatalogo.viewsmodel.CatalogoViewModel
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class CatalogoActivity : AppCompatActivity() {
    private var dialog: ProgressDialogUtil? = null
    private lateinit var adapter: CatalogoDataAdapter
    private lateinit var binding: ActivityCatalogoBinding

    private val viewModel: CatalogoViewModel by viewModels()

    private var account: Auth0 = Auth0(
        ApplicationLocate.instance.dotenv[Constantes.COM_AUTH0_CLIENT_ID],
        ApplicationLocate.instance.dotenv[Constantes.COM_AUTH0_DOMAIN],
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCatalogoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        EventBus.getDefault().register(this)

        dialog = ProgressDialogUtil(this)

        adapter = CatalogoDataAdapter(
            object : CatalogoDataAdapter.OnItemTouchListener {
                override fun onDetalhar(view: View, position: Int) {

                    val catalogoSelecionado = adapter.getItem(position)

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
            adapter,
            false
        )

        viewModel.registros.onEach {
            adapter.carregarRegistros(it)
        }.launchIn(lifecycleScope)
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
                viewModel.atualizarRegistros(this,this)
                true
            }

            R.id.menu_central_logout -> {
                logout()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
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
