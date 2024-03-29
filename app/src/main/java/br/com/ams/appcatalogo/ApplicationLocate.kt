package br.com.ams.appcatalogo

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import br.com.ams.appcatalogo.common.Constantes
import com.blankj.utilcode.util.LogUtils
import com.imagekit.android.ImageKit
import com.imagekit.android.entity.TransformationPosition
import com.imagekit.android.entity.UploadPolicy
import dagger.hilt.android.HiltAndroidApp
import io.github.cdimascio.dotenv.dotenv
import javax.inject.Inject

@HiltAndroidApp
class ApplicationLocate : Application(), Configuration.Provider  {

    val dotenv = dotenv {
        directory = "./assets"
        filename = "env"
    }

    @Inject lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()

        instance = this

        ImageKit.init(
            context = applicationContext,
            publicKey = dotenv[Constantes.IMAGEKIT_PUBLICKEY],
            urlEndpoint = dotenv[Constantes.IMAGEKIT_ENDPOINT],
            transformationPosition = TransformationPosition.PATH,
            defaultUploadPolicy = UploadPolicy.Builder()
                .requireNetworkType(UploadPolicy.NetworkType.ANY)
                .maxRetries(3)
                .build()
        )

        com.blankj.utilcode.util.Utils.init(this)
        initLog()
    }

    fun initLog() {
        val config = LogUtils.getConfig()
            //MANTER ATIVO
            .setLogSwitch(true)
            //MANTER ATIVO
            .setConsoleSwitch(true)
            .setGlobalTag(null)
            .setLogHeadSwitch(true)
            //MANTER ATIVO
            .setLog2FileSwitch(true)
            //Esta dando erro na versão 1.20.4 com mudança do path,
            //por default o cache vai para /cache/log/
            //todos os processos foi redirecionado para esse path
            //.setDir(Constantes.INSTANCE.getPATH_AMS_LOG())
            //.setDir("")
            .setFilePrefix("txt")
            .setSingleTagSwitch(true)
            .setBorderSwitch(true)
            .setConsoleFilter(LogUtils.V)
            .setFileFilter(LogUtils.V)
            .setStackDeep(1)
            .setStackOffset(0)
            .setSaveDays(3)
        LogUtils.d(config.toString())
    }

    companion object {
        lateinit var instance: ApplicationLocate
            private set

      /*  lateinit var component: AppComponent
            private set*/
    }
}