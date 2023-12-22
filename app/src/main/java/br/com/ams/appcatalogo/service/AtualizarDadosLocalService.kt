package br.com.ams.appcatalogo.service

import android.content.Context
import br.com.ams.appcatalogo.R
import br.com.ams.appcatalogo.common.CustomException
import br.com.ams.appcatalogo.retrofit.RetrofitConfig
import com.blankj.utilcode.util.FileIOUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.NetworkUtils
import java.io.File

class AtualizarDadosLocalService(
    val context: Context,
    val onSucesso: () -> Unit,
    val onError: (e: Throwable) -> Unit
) {
    fun iniciar() {

        if (!NetworkUtils.isConnected()) {
            onError(CustomException(context.getString(R.string.erro_conexao_indisponivel)))
            return
        }

        baixarArquivo()
    }

    private fun baixarArquivo() {
        val newFile = File("/tmp/file.zip")

        val cag = RetrofitConfig.instance.getCargaArquivo().obterCarga()
        val execute = cag.execute()
        if(execute.isSuccessful){
            LogUtils.w(execute.message())
            FileIOUtils.writeFileFromIS(newFile,execute.body()!!.byteStream())
        }
        //  val isStream =  cag.byteStream()
        // FileIOUtils.writeFileFromIS(File(""),isStream)
    }

}