package br.com.ams.appcatalogo

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.ams.appcatalogo.catalogo.CatalogoActivity
import br.com.ams.appcatalogo.common.Constantes
import br.com.ams.appcatalogo.common.DateTimeUtil
import br.com.ams.appcatalogo.common.DialogsUtils
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import java.util.Date

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    var account: Auth0 = Auth0(
        ApplicationLocate.instance.dotenv[Constantes.COM_AUTH0_CLIENT_ID],
        ApplicationLocate.instance.dotenv[Constantes.COM_AUTH0_DOMAIN],
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ImageViewContent()
                }
            }
        }

        val policy = StrictMode.ThreadPolicy.Builder()
            .permitAll().build()
        StrictMode.setThreadPolicy(policy)


        //LIBERA IMAGENS
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        this.verificarPermissoes()
    }

    @Composable
    fun ImageViewContent() {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                modifier = Modifier.width(64.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }

    private fun verificarPermissoes() {
        /*
            Por algum motivo não pega na API 21 talvez na API >= 23
            ActivityCompat.requestPermissions(this, arrayOf("android.permission.WRITE_EXTERNAL_STORAGE"), 1);
         */
        val permissions = PermissionUtils.getPermissions().toMutableList()

        if (Build.VERSION.SDK_INT < 28) {
            //permissions.remove("android.permission.FOREGROUND_SERVICE")
        }

        PermissionUtils.permission(*permissions.toTypedArray())
            .callback { isAllGranted, _, deniedForever, denied ->
                if (isAllGranted) {
                    iniciarMain()
                } else {
                    var msg = ""
                    if (!denied.isNullOrEmpty()) {
                        LogUtils.e(denied)
                        msg = "Negadas: " + denied.joinToString("\t")
                    }
                    if (!deniedForever.isNullOrEmpty()) {
                        LogUtils.e(deniedForever)
                        if (!msg.equals("")) msg += "\n"
                        msg =
                            "Negadas Para Sempre: " + deniedForever.joinToString("\t")
                    }
                    if (!msg.equals("") && !AppUtils.isAppDebug()) {
                        if (!SPUtils.getInstance().getBoolean("IGNORAR_PERMISSOES", false)) {
                            DialogsUtils.showConfirmacao(
                                this@SplashActivity,
                                "Existe Permissões Negadas",
                                "Deseja ir para as permisões?\n" + msg,
                                {
                                    if (it) {
                                        PermissionUtils.launchAppDetailsSettings()
                                    } else {
                                        SPUtils.getInstance().put("IGNORAR_PERMISSOES", true)
                                        iniciarMain()
                                    }
                                })
                        } else {
                            iniciarMain()
                        }
                    } else {
                        iniciarMain()
                    }
                }
            }
            .request()
    }

    fun iniciarMain() {
        val sp = SPUtils.getInstance()
        if (!sp.contains(Constantes.KEY_TOKEN_BEARER) ||
            sp.getString(
                Constantes.KEY_TOKEN_BEARER
            ).isNullOrBlank()
            || tokenExpirado()
        ) {
            loginWithBrowser()
        } else {
            carregarViewPrincipal()
        }
    }

    fun tokenExpirado(): Boolean {
        val sp = SPUtils.getInstance()
        if (!sp.contains(Constantes.KEY_TOKEN_EXPIRESAT))  return true
        val expiresAt = sp.getString(Constantes.KEY_TOKEN_EXPIRESAT, "")
        if (expiresAt.isNullOrEmpty())   return true
        val dataExpiresAt = DateTimeUtil.stringDataISO8601(expiresAt)
        return Date().after(dataExpiresAt)
    }

    private fun loginWithBrowser() {
        // Setup the WebAuthProvider, using the custom scheme and scope.
        WebAuthProvider.login(account)
            .withScheme(getString(R.string.com_auth0_scheme))
            .withScope("openid profile email read:current_user update:current_user_metadata")
            .withAudience(ApplicationLocate.instance.dotenv[Constantes.COM_AUTH0_AUDIENCE])
            // Launch the authentication passing the callback where the results will be received
            .start(this, object : Callback<Credentials, AuthenticationException> {
                // Called when there is an authentication failure
                override fun onFailure(exception: AuthenticationException) {
                    // Something went wrong!
                    LogUtils.e(exception.message)
                    ToastUtils.showLong(R.string.erro_processo)
                    //finish()
                }

                // Called when authentication completed successfully
                override fun onSuccess(credentials: Credentials) {
                    // Get the access token from the credentials object.
                    // This can be used to call APIs
                    val sp = SPUtils.getInstance()
                    sp.put(Constantes.KEY_TOKEN_BEARER, credentials.accessToken)
                    sp.put(Constantes.KEY_TOKEN_USER_ID, credentials.user.getId())
                    sp.put(
                        Constantes.KEY_TOKEN_EXPIRESAT,
                        DateTimeUtil.dataStringISO8601(credentials.expiresAt)
                    )
                    carregarViewPrincipal()
                }
            })
    }

    private fun carregarViewPrincipal() {
        ActivityUtils.startActivity(CatalogoActivity::class.java)
        this.finish()
    }
}