package br.com.ams.appcatalogo.service

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Environment
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.Worker
import androidx.work.WorkerParameters
import br.com.ams.appcatalogo.ApplicationLocate
import br.com.ams.appcatalogo.database.AppDatabase
import br.com.ams.appcatalogo.database.VERSION_DB
import br.com.ams.appcatalogo.retrofit.RetrofitConfig
import com.blankj.utilcode.util.FileIOUtils
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ZipUtils
import java.io.File
import java.util.UUID
import javax.inject.Inject

const val TAG_ATUALIZAR_DADOS_WORK = "ATUALIZAR_DADOS_WORK"

class AtualizarDadosServiceWorker(
    private val appContext: Context,
    private val workerParams: WorkerParameters
) :
    Worker(appContext, workerParams) {

    @Inject
    lateinit var providesRoomDatabase: AppDatabase

    private lateinit var registrosAtualizaTabelas: ArrayList<AtualizaTabela>
    private lateinit var registrosAtualizaCampos: ArrayList<AtualizaCampo>

    private val dirDownload =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

    override fun doWork(): Result {

        registrosAtualizaTabelas = ArrayList()
        registrosAtualizaCampos = ArrayList()

        if (!FileUtils.createOrExistsDir(dirDownload)) {
            LogUtils.e("Não foi possivel criar o diretorio de download")
            return Result.failure()
        }

        ApplicationLocate.component.inject(this)

        val nomArquivoDb = baixarArquivo() ?: return Result.failure()
        LogUtils.w(nomArquivoDb)
        try {

            carregarValores(
                nomArquivoDb
            )

            atualizarRegistros()

        } catch (e: Exception) {
            LogUtils.e(e)
            return Result.failure()
        } finally {
            LogUtils.w("Removendo arquivo ${nomArquivoDb}")
            FileUtils.delete(nomArquivoDb)
        }

        return Result.success()
    }

    companion object {
        fun iniciar(context: Context): UUID {
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

    private fun baixarArquivo(): File? {
        val newFile = File(
            dirDownload
                .toString() +
                    File.separator + UUID.randomUUID().toString() + ".zip"
        )

        val cag = RetrofitConfig.instance.getCargaArquivo().obterCarga()
        val execute = cag.execute()
        if (execute.isSuccessful) {
            val isArquivoCriado =
                FileIOUtils.writeFileFromIS(newFile, execute.body()!!.byteStream())

            if (isArquivoCriado) {

                try {
                    val filesDb = ZipUtils.unzipFile(
                        newFile,
                        dirDownload
                    )

                    if (!filesDb.isNullOrEmpty()) {
                        return filesDb.get(0)
                    }
                } finally {
                    LogUtils.w("Removendo arquivo ${newFile}")
                    //FileUtils.delete(newFile)
                }

            }
        }
        return null
    }

    private fun carregarValores(file: File) {
        with(SQLiteDatabase.openOrCreateDatabase(file, null)) {
            this.use {
                with(
                    this.rawQuery(
                        "SELECT * FROM mapeamentoTabela WHERE versao <= ${VERSION_DB} ORDER BY sequencia",
                        null
                    )
                ) {
                    this.use {
                        while (it.moveToNext()) {
                            val id = getLong(getColumnIndexOrThrow("id"))
                            val tabela = getString(getColumnIndexOrThrow("tabela"))
                            registrosAtualizaTabelas.add(AtualizaTabela(id, tabela))
                        }
                    }
                }

                with(
                    this.rawQuery(
                        "SELECT * FROM mapeamentoCampos WHERE versao <= ${VERSION_DB}",
                        null
                    )
                ) {
                    this.use {
                        while (it.moveToNext()) {
                            val id = getLong(getColumnIndexOrThrow("id"))
                            val campo = getString(getColumnIndexOrThrow("campo"))
                            registrosAtualizaCampos.add(AtualizaCampo(id, campo))
                        }
                    }
                }

                registrosAtualizaTabelas.forEach { tabelaAtualiza ->
                    with(this.rawQuery("SELECT * FROM ${tabelaAtualiza.tabela}", null)) {
                        this.use {
                            while (it.moveToNext()) {

                                val contentValue = ContentValues()
                                tabelaAtualiza.valores.add(contentValue)

                                registrosAtualizaCampos.stream()
                                    .filter { campo -> campo.id == tabelaAtualiza.id }
                                    .forEach { campo ->



                                        when (getType(getColumnIndexOrThrow(campo.campo))) {
                                            Cursor.FIELD_TYPE_STRING -> {
                                                contentValue.put(
                                                    campo.campo,
                                                    getString(getColumnIndexOrThrow(campo.campo))
                                                )
                                            }

                                            Cursor.FIELD_TYPE_FLOAT -> {
                                                contentValue.put(
                                                    campo.campo,
                                                    getFloat(getColumnIndexOrThrow(campo.campo))
                                                )
                                            }

                                            Cursor.FIELD_TYPE_INTEGER -> {
                                                contentValue.put(
                                                    campo.campo,
                                                    getInt(getColumnIndexOrThrow(campo.campo))
                                                )
                                            }

                                            else -> contentValue.putNull(campo.campo)
                                        }
                                    }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun atualizarRegistros() {
        this.providesRoomDatabase.runInTransaction(Runnable {
            run {

            }
        })
        with(
            SQLiteDatabase.openDatabase(
                this.providesRoomDatabase.openHelper.writableDatabase.path.toString(),
                null,
                SQLiteDatabase.OPEN_READWRITE
            )
        ) {
            try {
                this.beginTransaction()
                registrosAtualizaTabelas.forEach { tabelaAtualiza ->
                    tabelaAtualiza.valores.forEach {
                        try {
                            this.insertWithOnConflict(
                                tabelaAtualiza.tabela,
                                "id",
                                it,
                                SQLiteDatabase.CONFLICT_REPLACE
                            )
                        } catch (e: Exception) {
                            LogUtils.e(tabelaAtualiza.tabela, it)
                            throw Exception("Erro inserir o registro " + it + " da tabela ${tabelaAtualiza.tabela}")
                        }
                    }
                }
                this.setTransactionSuccessful()
            } finally {
                this.endTransaction()
            }
        }
    }

    class AtualizaCampo(val id: Long, val campo: String)

    class AtualizaTabela(
        val id: Long,
        val tabela: String,
        var valores: ArrayList<ContentValues> = ArrayList()
    )
}