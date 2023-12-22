package br.com.ams.appcatalogo.common

import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.PathUtils
import com.blankj.utilcode.util.SPUtils

class Config {

    private val diretorios = arrayOf(
        PATH_AMS_TEMP
    )

    fun criarDiretorios() {
        if (!FileUtils.createOrExistsDir(PATH_AMS)) {
            LogUtils.e("Não foi possivel criar diretorio principal: " + PATH_AMS)
            throw Exception("Não foi possivel criar diretorio principal: " + PATH_AMS)
        }
        for (dir in diretorios) {
            FileUtils.createOrExistsDir(dir)
        }
    }

    companion object {
        val PATH_LOCAL: String
        val PATH_AMS: String
        val FILE_SEP: String = System.getProperty("file.separator")!!
        val LINE_SEP: String = System.getProperty("line.separator")!!
        var URL_SERVIDOR: String
        val PATH_AMS_TEMP: String

        init {
            var pah = PathUtils.getExternalAppFilesPath()
            if (pah == null || pah.equals("")) {
                pah = PathUtils.getExternalAppFilesPath()
            }
            PATH_LOCAL = pah + FILE_SEP
            PATH_AMS = PATH_LOCAL + FILE_SEP + "appcatalogo"
            PATH_AMS_TEMP = PATH_AMS + FILE_SEP + "tmp" + FILE_SEP

            URL_SERVIDOR = SPUtils.getInstance()
                .getString(Constantes.KEY_URL_SERVIDOR, Constantes.URL_SERVIDOR)

            Config().criarDiretorios()
        }
    }
}