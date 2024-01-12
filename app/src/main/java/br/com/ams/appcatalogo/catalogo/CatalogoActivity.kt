package br.com.ams.appcatalogo.catalogo

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import br.com.ams.appcatalogo.ApplicationLocate
import br.com.ams.appcatalogo.R
import br.com.ams.appcatalogo.common.*
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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.com.ams.appcatalogo.entity.Catalogo
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest

@AndroidEntryPoint
class CatalogoActivity : AppCompatActivity() {
    private val viewModel: CatalogoViewModel by viewModels()

    private var account: Auth0 = Auth0(
        ApplicationLocate.instance.dotenv[Constantes.COM_AUTH0_CLIENT_ID],
        ApplicationLocate.instance.dotenv[Constantes.COM_AUTH0_DOMAIN],
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                CatalogoListView { catalogoSelecionado ->
                    CatalogoPaginaFragment.newInstance(
                        catalogoSelecionado.id,
                        catalogoSelecionado.descricao,
                        catalogoSelecionado.identificador!!
                    ).openDialog(supportFragmentManager)
                }
            }
        }
        EventBus.getDefault().register(this)
    }

    @Composable
    fun CatalogoListView(onItemClick: (Catalogo) -> Unit) {
        val registros = viewModel.registros.collectAsState()
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 5.dp, vertical = 5.dp)
        ) {
            items(count = registros.value.size) { countValue ->
                val registro = registros.value[countValue]
                CatalogoListViewItem(registro, onItemClick)
            }
        }
    }

    @Composable
    fun CatalogoListViewItem(registro: Catalogo, onItemClick: (Catalogo) -> Unit) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onItemClick(registro)
                },
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
        ) {
            Row(modifier = Modifier.padding(10.dp)) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("${ApplicationLocate.instance.dotenv[Constantes.IMAGEKIT_ENDPOINT]}/catalogo/catalogos/${registro.identificador}/${registro.avatar!!}")
                        .crossfade(true)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .build(),
                    contentDescription = stringResource(R.string.app_name),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(50.dp),
                )
                Text(text = "${registro.descricao}", modifier = Modifier.padding(start = 5.dp))
            }
        }
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
                viewModel.atualizarRegistros(this, this)
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
