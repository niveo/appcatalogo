package br.com.ams.appcatalogo.service

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import java.util.UUID
import javax.inject.Singleton

@Singleton
class AtualizarDadosServiceWorkerStarter(var context: Context) {

    fun iniciar( ): UUID {
        val uploadWorkRequest: WorkRequest =
            OneTimeWorkRequestBuilder<AtualizarDadosServiceWorker>()
                .addTag(TAG_ATUALIZAR_DADOS_WORK)
                .build()
        WorkManager
            .getInstance(context)
            .enqueue(uploadWorkRequest)
        return uploadWorkRequest.id
    }

}