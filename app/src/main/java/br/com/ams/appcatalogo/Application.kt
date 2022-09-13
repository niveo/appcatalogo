package br.com.ams.appcatalogo

import android.app.Application
import com.blankj.utilcode.util.LogUtils

class Application : Application() {

    override fun onCreate() {
        super.onCreate()
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
}