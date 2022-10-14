package br.com.ams.appcatalogo.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import br.com.ams.appcatalogo.common.Constantes
import br.com.ams.appcatalogo.databinding.ActivityCatalogoBinding
import br.com.ams.appcatalogo.databinding.ActivityConfiguracaoBinding
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils

class ConfiguracaoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfiguracaoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfiguracaoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txtConfiguracaoServidor.setText(
            SPUtils.getInstance().getString(Constantes.KEY_URL_SERVIDOR)
        )

        binding.txtConfiguracaoTimeOut.setText(
            SPUtils.getInstance()
                .getInt(Constantes.KEY_TIME_OUT_CONEXAO, Constantes.TIME_OUT_CONEXAO).toString()
        )

        binding.btnConfiguracaoSalvarSair.setOnClickListener {
            if (TextUtils.isEmpty(binding.txtConfiguracaoServidor.text) ||
                TextUtils.isEmpty(binding.txtConfiguracaoTimeOut.text)
            ) {
                ToastUtils.showLong("Informe o servidor e o Tempo de Conexão.")
                return@setOnClickListener
            }
            SPUtils.getInstance().put(
                Constantes.KEY_URL_SERVIDOR,
                binding.txtConfiguracaoServidor.text.toString(),
                true
            )
            SPUtils.getInstance().put(
                Constantes.KEY_TIME_OUT_CONEXAO,
                binding.txtConfiguracaoTimeOut.text.toString().toInt(),
                true
            )
            AppUtils.relaunchApp(true)
        }
    }
}