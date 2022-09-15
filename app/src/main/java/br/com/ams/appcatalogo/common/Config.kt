package br.com.ams.appcatalogo.common

import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.PathUtils

class Config {

    private val diretorios = arrayOf(
        PATH_AMS_TEMP,
        PATH_AMS_CATALOGO
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
        val PATH_AMS_CATALOGO: String
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
            PATH_AMS_CATALOGO = PATH_AMS + FILE_SEP + "catalogo" + FILE_SEP
            PATH_AMS_TEMP = PATH_AMS + FILE_SEP + "tmp" + FILE_SEP

            URL_SERVIDOR = "http://192.168.0.129:8080"

            Config().criarDiretorios()
        }
    }
}