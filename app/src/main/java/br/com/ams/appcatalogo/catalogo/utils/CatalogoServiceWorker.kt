package br.com.ams.appcatalogo.catalogo.utils

import android.annotation.SuppressLint
import android.content.Context
import androidx.work.*
import br.com.ams.appcatalogo.ApplicationLocate
import br.com.ams.appcatalogo.common.Config
import br.com.ams.appcatalogo.common.CustomException
import br.com.ams.appcatalogo.common.DateTimeUtil
import br.com.ams.appcatalogo.common.DialogsUtils
import br.com.ams.appcatalogo.model.CatalogoPaginaMapeadosDTO
import br.com.ams.appcatalogo.retrofit.RetrofitConfig
import com.blankj.utilcode.util.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CatalogoServiceWorker(var appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {



    override suspend fun doWork(): Result {

        LogUtils.d("CatalogoServiceWorker")

        if (runAttemptCount > 1) {
            return Result.failure(
                Data.Builder().putStringArray("ERRO", arrayOf("Limite de falhas"))
                    .build()
            )
        }

        EventBus.getDefault().post(MensagemBusCatalogo(true, false))

        val loc = suspendCoroutine { continuation ->
            analisarCatalogo(inputData.getLong("CODIGO", 0), {
                EventBus.getDefault().post(MensagemBusCatalogo(false, true))
                continuation.resume(null)
            }, {
                LogUtils.e(it)
                EventBus.getDefault().post(MensagemBusCatalogo(false, false))
                continuation.resume(it)
            })
        }

        if (loc == null) {
            return Result.success()
        } else {
            if (loc is CustomException) {
                ToastUtils.showLong(loc.message)
                return Result.failure()
            } else {
                ToastUtils.showLong(br.com.ams.appcatalogo.R.string.erro_comnova_tentativa)
                return Result.retry()
            }
        }
    }

    fun analisarCatalogo(
        codigoCatalogo: Long,
        sucess: () -> Unit,
        error: (error: Throwable) -> Unit
    ) {

        val registros = ApplicationLocate.instance.dataBase.catalogoPaginaDao()
            .obterCatalogoPaginaMapeados(codigoCatalogo)

        if (registros.isNullOrEmpty()) {
            error(Exception("Não existe registro no catalogo " + codigoCatalogo))
            return
        }

        val dirCatalogo =
            File(Config.PATH_AMS_CATALOGO + Config.FILE_SEP + codigoCatalogo)

        if (!dirCatalogo.exists()) {
            validarDiretorio(codigoCatalogo, dirCatalogo, sucess, error)
        } else {
            validarRegistros(codigoCatalogo, registros, dirCatalogo, sucess, error)
        }
    }

    private fun validarDiretorio(
        codigoCatalogo: Long,
        dirCatalogo: File,
        sucess: () -> Unit,
        error: (error: Throwable) -> Unit
    ) {
        if (!NetworkUtils.isConnected()) {
            error(CustomException(appContext.getString(br.com.ams.appcatalogo.R.string.erro_conexao_indisponivel)))
            return
        }
        RetrofitConfig.instance.getCatalogoArquivoService()
            .downloadCatalogo(codigoCatalogo)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val f = File(
                    Config.PATH_AMS_TEMP + File.separator + UUID.randomUUID()
                        .toString() + ".zip"
                )
                FileIOUtils.writeFileFromBytesByStream(f, it.bytes())
                ZipUtils.unzipFile(f, dirCatalogo)
                try {
                    FileUtils.delete(f)
                } catch (e: Exception) {
                }
                sucess()
            }, {
                LogUtils.e(it)
                error(it)
            })

        /*val channel = UtilGrpc.newChannel()
        Rx3CatalogoServiceGrpc.newRxStub(channel)
            .downloadCatalogo(
                CatalogoRequest.newBuilder().setCodigoCatalogo(codigoCatalogo.toInt()).build()
            ).compose(UtilGrpc.applySchedulersSingleTransformer(channel))
            .subscribe({
                val f = File(
                    Config.PATH_AMS_TEMP + File.separator + UUID.randomUUID()
                        .toString() + ".zip"
                )
                FileIOUtils.writeFileFromBytesByStream(f, it.data.toByteArray())
                ZipUtils.unzipFile(f, dirCatalogo)
                try {
                    FileUtils.delete(f)
                } catch (e: Exception) {
                }
                sucess()
            }, {
                LogUtils.e(it)
                error(it)
            })*/
    }

    private fun validarRegistros(
        codigoCatalogo: Long,
        registros: List<CatalogoPaginaMapeadosDTO>,
        dirCatalogo: File,
        sucess: () -> Unit, error: (error: Throwable) -> Unit
    ) {
         val mapPaginas = ArrayList<String>()
         for (pagina in registros) {
             val dataFormat = DateTimeUtil.dataPatterBR(pagina.dataAlterado, "yyyyMMddHHmmss")
             val fileCatalogo = File(
                 dirCatalogo.absoluteFile.toString() + Config.FILE_SEP
                         + pagina.codigo + "_" + dataFormat
             )
             try {
                 FileUtils.deleteFilesInDirWithFilter(
                     dirCatalogo.absoluteFile.toString() + Config.FILE_SEP
                 ) { pathname ->
                     pathname.name.startsWith(pagina.codigo.toString() + "_") &&
                             !pathname.toString()
                                 .equals(fileCatalogo.toString())
                 }
             } catch (e: Exception) {
                 LogUtils.e(e)
             }
             if (!fileCatalogo.exists()) {
                 mapPaginas.add(pagina.codigo.toString())
             }
         }

         if (mapPaginas.isNullOrEmpty()) {
             //error(Exception("Paginas não informadas."))
             sucess()
             return
         }

         val paginasBaixar = mapPaginas.joinToString(
             separator = ",",
             prefix = "[",
             postfix = "]"
         ) { it }

         if (!NetworkUtils.isConnected()) {
             error(CustomException(appContext.getString(br.com.ams.appcatalogo.R.string.erro_conexao_indisponivel)))
             return
         }

        LogUtils.d(paginasBaixar)

        LogUtils.d(codigoCatalogo)

        RetrofitConfig.instance.getCatalogoArquivoService()
            .downloadCatalogoPaginas(codigoCatalogo, paginasBaixar)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val f = File(
                    Config.PATH_AMS_TEMP + File.separator + UUID.randomUUID()
                        .toString() + ".zip"
                )
                FileIOUtils.writeFileFromBytesByStream(f, it.bytes())
                ZipUtils.unzipFile(f, dirCatalogo)
                try {
                    FileUtils.delete(f)
                } catch (e: Exception) {
                }
                sucess()
            }, {
                LogUtils.e(it)
                error(it)
            })

       /*  val channel = UtilGrpc.newChannel()
         Rx3CatalogoServiceGrpc.newRxStub(channel)
             .downloadCatalogoPaginas(
                 CatalogoPaginaRequest.newBuilder().setCodigoCatalogo(codigoCatalogo.toInt())
                     .setCodigoCatalogoPagina(paginasBaixar).build()
             ).compose(UtilGrpc.applySchedulersSingleTransformer(channel))
             .subscribe({
                 val f = File(
                     Config.PATH_AMS_TEMP + File.separator + UUID.randomUUID()
                         .toString() + ".zip"
                 )
                 FileIOUtils.writeFileFromBytesByStream(f, it.data.toByteArray())
                 ZipUtils.unzipFile(f, dirCatalogo)
                 try {
                     FileUtils.delete(f)
                 } catch (e: Exception) {
                 }
                 sucess()

             }, {
                 LogUtils.e(it)
                 error(it)
             })*/
    }

    companion object {
        @SuppressLint("MissingPermission")
        fun iniciar(context: Context, codigoCatalogo: Long) {

            val dirCatalogo =
                File(Config.PATH_AMS_CATALOGO + Config.FILE_SEP + codigoCatalogo)

            val files = dirCatalogo.listFiles()

            if (!NetworkUtils.isWifiConnected() && !dirCatalogo.exists() && files.isNullOrEmpty()) {
                DialogsUtils.showConfirmacao(
                    context,
                    "Alerta",
                    "Ative o WIFI para não consumir seu pacote de dados, deseja baixar o catalogo mesmo assim?",
                    {
                        if (it) {
                            iniciarServico(context, codigoCatalogo)
                        }
                    })
            } else {
                iniciarServico(context, codigoCatalogo)
            }
        }

        private fun iniciarServico(context: Context, codigoCatalogo: Long) {
            val uploadWorkRequest =
                OneTimeWorkRequestBuilder<CatalogoServiceWorker>()
                    .setBackoffCriteria(
                        BackoffPolicy.LINEAR,
                        OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
                        TimeUnit.MILLISECONDS
                    )
                    .setInputData(Data.Builder().putLong("CODIGO", codigoCatalogo).build())
                    .build()

            val workM = WorkManager.getInstance(context)
                .beginUniqueWork(
                    "WORK_NAME_ATUALIZAR_CATALOGO",
                    ExistingWorkPolicy.REPLACE,
                    uploadWorkRequest
                )
            workM.enqueue()
        }
    }
}