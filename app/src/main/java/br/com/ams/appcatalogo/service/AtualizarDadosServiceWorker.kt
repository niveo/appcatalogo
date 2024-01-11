package br.com.ams.appcatalogo.service

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Environment
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import br.com.ams.appcatalogo.database.AppDatabase
import br.com.ams.appcatalogo.database.VERSION_DB
import br.com.ams.appcatalogo.retrofit.RetrofitConfig
import com.blankj.utilcode.util.FileIOUtils
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ZipUtils
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID

const val TAG_ATUALIZAR_DADOS_WORK = "ATUALIZAR_DADOS_WORK"

@HiltWorker
class AtualizarDadosServiceWorker @AssistedInject constructor(
    private val providesRoomDatabase: AppDatabase,
    @Assisted private val appContext: Context,
    @Assisted private val workerParams: WorkerParameters,
) :
    CoroutineWorker(appContext, workerParams) {

    private lateinit var registrosAtualizaTabelas: ArrayList<AtualizaTabela>
    private lateinit var registrosAtualizaCampos: ArrayList<AtualizaCampo>

    private val dirDownload =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            registrosAtualizaTabelas = ArrayList()
            registrosAtualizaCampos = ArrayList()
            var nomArquivoDb: File? = null
            try {

                if (!FileUtils.createOrExistsDir(dirDownload)) {
                    LogUtils.e("Não foi possivel criar o diretorio de download")
                    throw Exception("Não foi possivel criar o diretorio de download")
                }

                nomArquivoDb = baixarArquivo()!!

                carregarValores(
                    nomArquivoDb
                )

                atualizarRegistros()

                Result.success()

            } catch (e: Exception) {
                LogUtils.e(e)
                Result.failure()
            } finally {
                LogUtils.w("Removendo arquivo ${nomArquivoDb}")
                FileUtils.delete(nomArquivoDb)
            }
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
                    LogUtils.i("Removendo arquivo ${newFile}")
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
        with(providesRoomDatabase.openHelper.writableDatabase) {
            try {
                this.beginTransaction()
                registrosAtualizaTabelas.forEach { tabelaAtualiza ->
                    tabelaAtualiza.valores.forEach {
                        try {
                         this.insert(tabelaAtualiza.tabela, SQLiteDatabase.CONFLICT_REPLACE, it)
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

    inner class AtualizaCampo(val id: Long, val campo: String)

    inner class AtualizaTabela(
        val id: Long,
        val tabela: String,
        var valores: ArrayList<ContentValues> = ArrayList()
    )
}