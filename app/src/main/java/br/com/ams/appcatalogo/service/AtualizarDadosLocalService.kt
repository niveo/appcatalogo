package br.com.ams.appcatalogo.service

import android.content.Context
import br.com.ams.appcatalogo.ApplicationLocate
import br.com.ams.appcatalogo.R
import br.com.ams.appcatalogo.common.CustomException
import br.com.ams.appcatalogo.entity.Catalogo
import br.com.ams.appcatalogo.entity.CatalogoPagina
import br.com.ams.appcatalogo.entity.CatalogoPaginaMapeamento
import br.com.ams.appcatalogo.entity.Produto
import br.com.ams.appcatalogo.retrofit.RetrofitConfig
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.NetworkUtils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers

class AtualizarDadosLocalService {
    fun iniciar(context: Context, onSucesso: () -> Unit, onError: (e: Throwable) -> Unit) {

        if (!NetworkUtils.isConnected()) {
            onError(CustomException(context.getString(R.string.erro_conexao_indisponivel)))
            return
        }

    }

    private fun baixarArquivo(){
        //RetrofitConfig.instance.getCargaArquivo().obterCarga().
    }

}