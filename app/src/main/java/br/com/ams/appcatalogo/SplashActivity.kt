package br.com.ams.appcatalogo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.ams.appcatalogo.catalogo.CatalogoActivity
import br.com.ams.appcatalogo.common.Constantes
import br.com.ams.appcatalogo.common.DialogsUtils
import br.com.ams.appcatalogo.view.ConfiguracaoActivity
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

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var account: Auth0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        account = Auth0(
            getString(R.string.com_auth0_client_id),
            getString(R.string.com_auth0_domain)
        )

        if (Build.VERSION.SDK_INT >= 23) {
            this.verificarPermissoes()
        } else {
            this.iniciarMain()
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
        if(!SPUtils.getInstance().getString(Constantes.KEY_TOKEN_BEARER,"").isNullOrBlank()){
            carregarViewPrincipal()
        } else {
            loginWithBrowser()
        }
    }

    private fun loginWithBrowser() {
        // Setup the WebAuthProvider, using the custom scheme and scope.
        WebAuthProvider.login(account)
            .withScheme(getString(R.string.com_auth0_scheme))
            .withScope("openid profile email read:current_user update:current_user_metadata")
            //.withAudience("https://${getString(R.string.com_auth0_domain)}/api/v2/")
            // Launch the authentication passing the callback where the results will be received
            .start(this, object : Callback<Credentials, AuthenticationException> {
                // Called when there is an authentication failure
                override fun onFailure(exception: AuthenticationException) {
                    // Something went wrong!
                    finish()
                }

                // Called when authentication completed successfully
                override fun onSuccess(credentials: Credentials) {
                    // Get the access token from the credentials object.
                    // This can be used to call APIs
                    val accessToken = credentials.accessToken
                    SPUtils.getInstance().put(Constantes.KEY_TOKEN_BEARER, accessToken)
                    carregarViewPrincipal()
                }
            })
    }

    private fun carregarViewPrincipal(){
        finish()
        ActivityUtils.startActivity(CatalogoActivity::class.java)
    }

    private fun logout() {
        WebAuthProvider.logout(account)
            .withScheme("demo")
            .start(this, object: Callback<Void?, AuthenticationException> {
                override fun onSuccess(payload: Void?) {
                    // The user has been logged out!
                }

                override fun onFailure(error: AuthenticationException) {
                    // Something went wrong!
                }
            })
    }

}