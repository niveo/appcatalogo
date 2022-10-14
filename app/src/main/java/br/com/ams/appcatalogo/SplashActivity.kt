package br.com.ams.appcatalogo

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.ams.appcatalogo.catalogo.CatalogoActivity
import br.com.ams.appcatalogo.common.Constantes
import br.com.ams.appcatalogo.common.DialogsUtils
import br.com.ams.appcatalogo.view.ConfiguracaoActivity
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.SPUtils

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val permissions = PermissionUtils.getPermissions().toMutableList()
        if (permissions.isEmpty()) {
            iniciarMain()
        } else {
            PermissionUtils.permission(*permissions.toTypedArray())
                .callback { isAllGranted, granted, deniedForever, denied ->
                    if (isAllGranted) {
                        iniciarMain()
                    } else {
                        DialogsUtils.showConfirmacao(
                            this@SplashActivity,
                            "Existe permissões negadas",
                            "Deseja ir para as permisões?",
                            {
                                if (it) {
                                    PermissionUtils.launchAppDetailsSettings()
                                } else {
                                    System.exit(0)
                                }
                            })
                    }
                }.request()
        }
    }

    fun iniciarMain() {
        if(!SPUtils.getInstance().contains(Constantes.KEY_URL_SERVIDOR) ||
            SPUtils.getInstance().getString(Constantes.KEY_URL_SERVIDOR) == ""){
            ActivityUtils.startActivity(Intent(this, ConfiguracaoActivity::class.java))
        } else {
            ActivityUtils.startActivity(Intent(this, CatalogoActivity::class.java))
        }
        finish()
    }
}