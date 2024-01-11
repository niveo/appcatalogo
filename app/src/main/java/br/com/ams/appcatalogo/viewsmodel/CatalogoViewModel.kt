package br.com.ams.appcatalogo.viewsmodel

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import br.com.ams.appcatalogo.R
import br.com.ams.appcatalogo.entity.Catalogo
import br.com.ams.appcatalogo.repository.CatalogoRepository
import br.com.ams.appcatalogo.service.AtualizarDadosServiceWorker
import br.com.ams.appcatalogo.service.AtualizarDadosServiceWorkerStarter
import br.com.ams.appcatalogo.service.TAG_ATUALIZAR_DADOS_WORK
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatalogoViewModel @Inject constructor(
    private val catalogoRepository: CatalogoRepository
) : ViewModel() {

    private var _registros = MutableStateFlow<List<Catalogo>>(arrayListOf())
    val registros = _registros.asStateFlow()

    init {
        carregarRegistros()
    }

    private fun carregarRegistros() {
        _registros.value = catalogoRepository.getAll()
    }

    fun atualizarRegistros(context: Context, lifecycleOwner: LifecycleOwner) {
        val id = AtualizarDadosServiceWorkerStarter(context).iniciar()
        val mWorkManager = WorkManager.getInstance(context)
        mWorkManager.getWorkInfoByIdLiveData(id)
            .observe(lifecycleOwner, Observer {
                if (it.state.isFinished) {
                    carregarRegistros()
                    ToastUtils.showLong(R.string.registros_atualizados)
                }
            })
    }
}