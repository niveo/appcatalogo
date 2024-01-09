package br.com.ams.appcatalogo.viewsmodel

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import br.com.ams.appcatalogo.entity.Catalogo
import br.com.ams.appcatalogo.repository.CatalogoRepository
import br.com.ams.appcatalogo.service.AtualizarDadosServiceWorker
import br.com.ams.appcatalogo.service.TAG_ATUALIZAR_DADOS_WORK
import com.blankj.utilcode.util.LogUtils
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.subscribe
import kotlinx.coroutines.launch


/*
    private lateinit var viewModel: CatalogoViewModel

        viewModel = ViewModelProvider(
            this,
            CatalogoViewModelFactory(this, catalogoRepository)
        ).get(CatalogoViewModel::class.java)

        viewModel.loadDataFromWorker(this)
 */
class CatalogoViewModel(
    private val context: Context,
    private val catalogoRepository: CatalogoRepository
) : ViewModel() {

    val registros = catalogoRepository.getAll()

    fun loadDataFromWorker(lifecycleOwner: LifecycleOwner) {

        val mWorkManager = WorkManager.getInstance(context)

        mWorkManager.getWorkInfosByTagLiveData(TAG_ATUALIZAR_DADOS_WORK)
            .observe(lifecycleOwner, Observer {
                LogUtils.w(it)

            })

    }
}