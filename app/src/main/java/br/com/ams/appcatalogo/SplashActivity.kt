package br.com.ams.appcatalogo

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.ams.appcatalogo.catalogo.CatalogoActivity
import br.com.ams.appcatalogo.common.DialogsUtils
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.PermissionUtils

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
        ActivityUtils.startActivity(Intent(this, CatalogoActivity::class.java))
        finish()
    }
}