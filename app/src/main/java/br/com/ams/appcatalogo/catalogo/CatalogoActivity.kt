package br.com.ams.appcatalogo.catalogo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.com.ams.appcatalogo.ApplicationLocate
import br.com.ams.appcatalogo.R
import br.com.ams.appcatalogo.common.Constantes
import br.com.ams.appcatalogo.entity.Catalogo
import br.com.ams.appcatalogo.ui.theme.CatalogoApplicationTheme
import br.com.ams.appcatalogo.viewsmodel.CatalogoPaginaViewModel
import br.com.ams.appcatalogo.viewsmodel.CatalogoViewModel
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CatalogoActivity : AppCompatActivity() {
    private val viewModel: CatalogoViewModel by viewModels()
    private val catalogoPaginaViewModel: CatalogoPaginaViewModel by viewModels()

    private var account: Auth0 = Auth0(
        ApplicationLocate.instance.dotenv[Constantes.COM_AUTH0_CLIENT_ID],
        ApplicationLocate.instance.dotenv[Constantes.COM_AUTH0_DOMAIN],
    )

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CatalogoApplicationTheme {
                val openDialogCatalogoPagina = remember { mutableStateOf(false) }
                val catalogo = remember { mutableStateOf<Catalogo?>(null) }
                val atualizando by viewModel.atualizandoRegistros.collectAsState()
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(stringResource(R.string.app_name))
                            }
                        )
                    },
                    floatingActionButton = {
                        ElevatedButton(enabled = !atualizando, onClick = {
                            viewModel.atualizarRegistros(this, this)
                        }) {
                            Icon(
                                Icons.Default.Refresh, contentDescription = "Atualizar"
                            )
                        }
                    },
                    content = {
                        CatalogoListView(Modifier.padding(it)) { catalogoSelecionado ->
                            catalogo.value = catalogoSelecionado
                            openDialogCatalogoPagina.value = true
                        }
                        when {
                            openDialogCatalogoPagina.value -> {
                                CatalogoPaginaCompose(
                                    catalogo.value!!,
                                    supportFragmentManager,
                                    catalogoPaginaViewModel
                                ).CatalogoPaginaDialog {
                                    openDialogCatalogoPagina.value = false
                                }
                            }
                        }
                    }
                )
            }
        }
    }


    @Composable
    fun CatalogoListView(modifier: Modifier, onItemClick: (Catalogo) -> Unit) {
        val registros = viewModel.registros.collectAsState()
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(5.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(count = registros.value.size) { countValue ->
                val registro = registros.value[countValue]
                Box(modifier = Modifier.padding(5.dp)) {
                    CatalogoListViewItem(registro, onItemClick)
                }
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
