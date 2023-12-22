package br.com.ams.appcatalogo.service

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.Worker
import androidx.work.WorkerParameters
import br.com.ams.appcatalogo.retrofit.RetrofitConfig
import com.blankj.utilcode.util.FileIOUtils
import com.blankj.utilcode.util.LogUtils
import java.io.File

class AtualizarDadosServiceWorker(var appContext: Context, var workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {
    override fun doWork(): Result {

        baixarArquivo()

        return Result.success()
    }

    companion object {
        fun iniciar(context: Context) {
            val uploadWorkRequest: WorkRequest =
                OneTimeWorkRequestBuilder<AtualizarDadosServiceWorker>()
                    .build()
            WorkManager
                .getInstance(context)
                .enqueue(uploadWorkRequest)
        }
    }

    private fun baixarArquivo() {
        val newFile = File("/tmp/file.zip")

        val cag = RetrofitConfig.instance.getCargaArquivo().obterCarga()
        val execute = cag.execute()
        if(execute.isSuccessful){
            FileIOUtils.writeFileFromIS(newFile,execute.body()!!.byteStream())
        }
        LogUtils.w(execute.message())
        //  val isStream =  cag.byteStream()
        // FileIOUtils.writeFileFromIS(File(""),isStream)
    }
}